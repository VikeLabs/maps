package ca.vikelabs.maps

import ca.vikelabs.maps.routes.ping
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.HttpHandler
import org.http4k.format.Jackson

val application: HttpHandler = contract {
    renderer = OpenApi3(
        apiInfo = ApiInfo("map uvic", "0.0.1", "An API for navigating around the University of Victoria."),
        Jackson
    )
    descriptionPath = "/"
    routes += ping()
}
