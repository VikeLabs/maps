package ca.vikelabs.maps.data.impl

import ca.vikelabs.maps.data.Building
import ca.vikelabs.maps.data.MapData
import ca.vikelabs.maps.opensteetmaps.types.OverpassResponse
import mu.KotlinLogging
import org.http4k.client.JavaHttpClient
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.format.Jackson.auto

private val logger = KotlinLogging.logger { }

class OpenStreetMapsOverpassMapData(
    private val client: HttpHandler = JavaHttpClient(),
    private val overpassInstanceEndpoint: Uri = Uri.of("https://lz4.overpass-api.de/api/interpreter")
) : MapData {
    private fun OverpassQuery.asRequest() = this.asRequest(overpassInstanceEndpoint)

    private val bodyLens = Body.auto<OverpassResponse>().toLens()

    override fun buildings(): List<Building> {
        val request = OverpassQuery.buildings.asRequest()
        val response = client(request)
        val overpassResponse = bodyLens(response)
        return overpassResponse.elements.mapNotNull { it.tags?.name?.let { name -> Building(name) } }
    }
}

@JvmInline
private value class OverpassQuery private constructor(private val query: String) {
    fun asRequest(uri: Uri): Request = Request(Method.GET, uri).query("data", this.query)

    companion object {
        val buildings = OverpassQuery(
            """
            [out:json];
                
            area[name="University of Victoria"]->.uvic;

            (
            way(area.uvic)[building];
            rel(area.uvic)[building];
            );


            (
              ._;
              >;
            );

            out;
        """.trimIndent()
        )
    }
}

