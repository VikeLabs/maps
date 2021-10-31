package ca.vikelabs.maps

import ca.vikelabs.maps.routes.ping
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.HttpHandler
import org.http4k.format.Jackson
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer


val rootContract = contract {
    renderer = OpenApi3(
        apiInfo = ApiInfo("map uvic", "0.0.1", "An API for navigating around the University of Victoria."),
        Jackson
    )
    descriptionPath = "/swagger.json"
    routes += ping()
}

val ping: HttpHandler = routes("/" bind rootContract)

fun main() {
    val server = ping.asServer(SunHttp())
    server.start()
    println("Server started and running on ${server.port()}.")
    server.block()
}