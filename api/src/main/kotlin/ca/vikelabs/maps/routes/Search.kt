package ca.vikelabs.maps.routes

import ca.vikelabs.maps.data.Building
import ca.vikelabs.maps.data.MapData
import ca.vikelabs.maps.extensions.levenshteinDistanceTo
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Query
import org.http4k.lens.nonEmptyString

object Search {
    val query = Query.nonEmptyString().required("query")
    val response = Body.auto<SearchResponse>().toLens()
}

fun search(
    mapsData: MapData = MapData()
): ContractRoute {

    val spec = "search" meta {
        summary = "searches the UVic campus based on a single search string"
        description = "searches for buildings with a levenshteinDistance of 1 to the query string"
        queries += Search.query
        produces += ContentType.APPLICATION_JSON
        returning(
            Status.OK,
            Search.response to SearchResponse(
                listOf(SearchResult("Elliott Building", Coordinate(48.4627526, -123.3108017)))
            ),
            "a single result"
        )
    } bindContract Method.GET

    val search: HttpHandler = { request ->
        val query = Search.query(request)
        val searchResults = mapsData
            .buildings()
            .filter { searchMatches(it, query) }
            .map { SearchResult(it) }
        Response(Status.OK).with(Search.response of SearchResponse(results = searchResults))
    }

    return spec to search
}

private fun searchMatches(it: Building, query: String): Boolean {
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

data class SearchResponse(val results: List<SearchResult>)

data class SearchResult(val name: String, val center: Coordinate) {
    constructor(building: Building) : this(
        name = building.name,
        center = building.bounds.reduce { acc, coordinates -> acc + coordinates } / building.bounds.size
    )
}
