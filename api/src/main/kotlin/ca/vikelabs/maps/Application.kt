package ca.vikelabs.maps

import ca.vikelabs.maps.routes.ping
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.HttpHandler
import org.http4k.format.Jackson
import org.http4k.server.SunHttp
import org.http4k.server.asServer

val application: HttpHandler = contract {
    renderer = OpenApi3(
        apiInfo = ApiInfo("map uvic", "0.0.1", "An API for navigating around the University of Victoria."),
        Jackson
    )
    descriptionPath = "/"
    routes += ping()
}

fun main() {
    val server = application.asServer(SunHttp())
    server.start()
    println("Server started and running on http://localhost:${server.port()}.")
    server.block()
}
