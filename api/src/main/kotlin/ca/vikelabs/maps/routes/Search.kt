package ca.vikelabs.maps.routes

import ca.vikelabs.maps.data.MapData
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

data class SearchResponseBody(val resultsFound: Boolean)

fun search(mapsData: MapData = MapData()): ContractRoute {
    val queryQuery = Query.string().required("query")

    val responseBodyLens = Body.auto<SearchResponseBody>().toLens()

    val spec = "search" meta {
        returning("successful search" to Response(Status.OK).with(responseBodyLens of SearchResponseBody(resultsFound = true)))
    } bindContract Method.GET

    val search: HttpHandler = { request ->
        val query = queryQuery(request)
        val foundBuildings = mapsData.buildings().find { it.name.lowercase() == query.lowercase() }
        Response(Status.OK).with(responseBodyLens of SearchResponseBody(resultsFound = foundBuildings != null))
    }

    return spec to search
}
