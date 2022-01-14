package ca.vikelabs.maps.routes

import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.Query
import org.http4k.lens.double


val toLatQuery = Query.double().required("toLat", "The goal latitude.")
val toLonQuery = Query.double().required("toLon", "The goal longitude.")
val fromLatQuery = Query.double().required("fromLat", "The initial latitude.")
val fromLonQuery = Query.double().required("fromLon", "The initial longitude.")

private val spec = "/route" meta {
    summary = "Route between two places."
    queries += toLatQuery
    queries += toLonQuery
    queries += fromLatQuery
    queries += fromLonQuery
}

fun route(): ContractRoute {
    return spec bindContract GET to ::impl
}

fun impl(): HttpHandler = handler@{ _: Request -> Response(Status.OK) }

