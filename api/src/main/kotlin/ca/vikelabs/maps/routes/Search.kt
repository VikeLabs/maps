package ca.vikelabs.maps.routes

import ca.vikelabs.maps.data.MapData
import ca.vikelabs.maps.extensions.levenshteinDistanceTo
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Query
import org.http4k.lens.nonEmptyString

class Search(private val mapData: MapData = MapData()) : HttpHandler {
    data class ResponseBody(val results: List<Building>) {
        data class Building(val name: String, val center: Coordinate) {
            constructor(building: ca.vikelabs.maps.data.Building) : this(
                name = building.name,
                center = building.center
            )
        }
    }

    companion object {
        val query = Query.nonEmptyString().required("query")
        val response = Body.auto<ResponseBody>().toLens()

        val spec = "search" meta {
            summary = "searches the UVic campus based on a single search string"
            description = "searches for buildings with a levenshteinDistance of 1 to the query string"
            queries += query
            returning(
                Status.OK,
                response to ResponseBody(
                    listOf(ResponseBody.Building("Elliott Building", Coordinate(48.4627526, -123.3108017)))
                ),
                "a single result"
            )
        } bindContract Method.GET
    }

    val contractRoute = spec to ::search
    override fun invoke(request: Request) = contractRoute(request)

    private fun search(): HttpHandler = { request ->
        val query = query(request)
        val searchResults = mapData
            .buildings()
            .filter { searchMatches(it, query) }
            .map { ResponseBody.Building(it) }
        Response(Status.OK).with(response of ResponseBody(results = searchResults))
    }
}

private fun searchMatches(it: ca.vikelabs.maps.data.Building, query: String): Boolean {
    val lowercaseName = it.name.lowercase()
    val lowercaseQuery = query.lowercase()

    val wholeNameMatches = lowercaseName.levenshteinDistanceTo(lowercaseQuery) <= 1

    val abbrNameEqual = it.abbrName?.lowercase() == lowercaseQuery

    val anyNameWordMatchesAnyQueryWord = lowercaseName
        .split(' ')
        .any { nameWord ->
            lowercaseQuery
                .split(' ')
                .any { queryWord -> nameWord.levenshteinDistanceTo(queryWord) <= 1 }
        }

    return wholeNameMatches || abbrNameEqual || anyNameWordMatchesAnyQueryWord
}

data class Coordinate(val latitude: Double, val longitude: Double) {
    operator fun plus(lhs: Coordinate) = Coordinate(
        latitude = this.latitude + lhs.latitude,
        longitude = this.longitude + lhs.longitude
    )

    operator fun div(lhs: Number) = Coordinate(
        latitude = latitude / lhs.toDouble(),
        longitude = longitude / lhs.toDouble()
    )
}
