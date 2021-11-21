package ca.vikelabs.maps.data.impl

import ca.vikelabs.maps.data.Building
import ca.vikelabs.maps.data.MapData
import ca.vikelabs.maps.opensteetmaps.types.OverpassElement
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
    private fun OverpassQuery.asRequest() = asRequest(overpassInstanceEndpoint)

    private val bodyLens = Body.auto<OverpassResponse>().toLens()

    override fun buildings(): List<Building> {
        val request = OverpassQuery.buildings.asRequest()

        // try to avoid this being a network call as much as possible, overpass is very slow.
        val response = client(request)

        // TODO: 2021-11-20 cache this somehow as it nearly takes a full second to parse the json
        val overpassResponse = bodyLens(response)
        return overpassResponse.elements.mapNotNull { overpassElement ->
            val coords = when (overpassElement.type) {
                "way" ->
                    overpassElement.nodes
                        ?.mapNotNull { overpassResponse.findElementByRef(it) }
                        ?.map { Coordinate(it.lat!!, it.lon!!) }
                "relation" -> {
                    val outerWayRef = overpassElement.members?.find { it.role == "outer" && it.type == "way" }
                        ?: return@mapNotNull null
                    val outerWay = overpassResponse.findElementByRef(outerWayRef.ref)
                        ?: return@mapNotNull null
                    outerWay.members?.mapNotNull { wayNode ->
                        overpassResponse
                            .findElementByRef(wayNode.ref)
                            ?.let { Coordinate(it.lat!!, it.lon!!) }
                    }
                }
                else -> null
            }
            val tags = overpassElement.tags ?: return@mapNotNull null
            val name = tags.name ?: return@mapNotNull null
            val nnCoords = coords ?: return@mapNotNull null
            Building(name, tags.abbrName, nnCoords)
        }
    }
}

private fun OverpassResponse.findElementByRef(ref: Number): OverpassElement? =
    this.elements.find { it.id == ref.toLong() }

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
