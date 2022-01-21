package ca.vikelabs.maps.routes

import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.Query
import org.http4k.lens.double


class Route : HttpHandler {
    companion object {
        val toLatQuery = Query.double().required("toLat", "The goal latitude.")
        val toLonQuery = Query.double().required("toLon", "The goal longitude.")
        val fromLatQuery = Query.double().required("fromLat", "The initial latitude.")
        val fromLonQuery = Query.double().required("fromLon", "The initial longitude.")

        val spec = "/route" meta {
            summary = "Route between two places."
            queries += toLatQuery
            queries += toLonQuery
            queries += fromLatQuery
            queries += fromLonQuery
        }
    }

    val contractRoute = spec bindContract GET to ::route
    override fun invoke(request: Request) = contractRoute(request)

    private fun route(): HttpHandler = handler@{ _: Request -> Response(Status.OK) }
}
