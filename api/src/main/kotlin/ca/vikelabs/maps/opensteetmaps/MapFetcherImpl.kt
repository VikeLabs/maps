package ca.vikelabs.maps.opensteetmaps

import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response

class MapFetcherImpl : MapFetcher {
    companion object {
        private const val overpassEndpoint = "https://lz4.overpass-api.de/api/interpreter"
    }

    /**
     * wrapper class to specify the string must be an overpass query
     */
    @JvmInline
    value class OverpassQuery(val string: String)

    val client = ApacheClient()

    fun getBuildings(): Response {
        val query = OverpassQuery(
            """
                [out:json][timeout:25];

                area["name"="University of Victoria"]->.uvic;

                (
                rel(area.uvic)["building"="university"];
                way(area.uvic)["building"="university"];
                );

                out geom;
                >;
                out skel qt;
            """.trimIndent()
        )
        return runQuery(query)
    }

    private fun runQuery(queryString: OverpassQuery): Response {
        return client(
            Request(Method.GET, overpassEndpoint)
                .query("data", queryString.string)
        )
    }
}

