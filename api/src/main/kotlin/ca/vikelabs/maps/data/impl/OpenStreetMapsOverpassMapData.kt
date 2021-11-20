package ca.vikelabs.maps.data.impl

import ca.vikelabs.maps.data.Building
import ca.vikelabs.maps.data.MapData
import ca.vikelabs.maps.opensteetmaps.types.OverpassResponse
import ca.vikelabs.maps.routes.Coordinate
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
        return overpassResponse.elements.mapNotNull { overpassElement ->
            val coords = when (overpassElement.type) {
                "way" -> overpassElement.nodes
                    ?.mapNotNull { node ->
                        overpassResponse.elements.find { it.id == node }
                            ?.let { Coordinate(it.lat!!, it.lon!!) }
                    }
                "relation" ->
                    overpassElement.members?.find { it.role == "outer" && it.type == "way" }?.let { outerWay ->
                        outerWay.ref.let { ref ->
                            overpassResponse.elements
                                .find { it.id == ref.toLong() }
                        }?.members
                            ?.mapNotNull { member ->
                                overpassResponse.elements.find { it.id == member.ref.toLong() }
                                    ?.let { Coordinate(it.lat!!, it.lon!!) }
                            }
                    }
                else -> null
            }
            overpassElement.tags?.let { tags -> coords?.let { coords -> tags.name?.let { name -> Building(name, tags.abbrName, coords) } } }
        }
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

