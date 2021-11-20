package ca.vikelabs.maps.routes

import ca.vikelabs.maps.data.MapData
import ca.vikelabs.maps.extensions.levenshteinDistanceTo
import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Query
import org.http4k.lens.string


data class SearchResponseBody(val resultsFound: Int)

fun search(mapsData: MapData = MapData()): ContractRoute {
    val queryQuery = Query.string().required("query")

    val responseBodyLens = Body.auto<SearchResponseBody>().toLens()

    val spec = "search" meta {
        summary = "searches the UVic campus based on a single search string"
        description = "searches for buildings with a levenshteinDistance of 1 to the query string"
        queries += queryQuery
        returning("successful search" to Response(Status.OK).with(responseBodyLens of SearchResponseBody(resultsFound = 1)))
        returning("fruitless search" to Response(Status.OK).with(responseBodyLens of SearchResponseBody(resultsFound = 0)))
    } bindContract Method.GET

    val search: HttpHandler = { request ->
        val query = queryQuery(request)
        val foundBuildings = mapsData
            .buildings()
            .filter { it.name.lowercase().levenshteinDistanceTo(query.lowercase()) <= 1 }
        Response(Status.OK).with(responseBodyLens of SearchResponseBody(resultsFound = foundBuildings.size))
    }

    return spec to search
}
