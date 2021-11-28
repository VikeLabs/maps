package ca.vikelabs.maps

import ca.vikelabs.maps.routes.ping
import ca.vikelabs.maps.routes.search
import mu.KotlinLogging
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.ApiServer
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.format.Jackson

private val logger = KotlinLogging.logger {}

val application by lazy { application(Config()) }

val warnAndDefault by lazy {
    logger.info { "No configuration given to application. Using default config" }
    Config()
}

fun application(
    config: Config = warnAndDefault
): HttpHandler = contract {
    renderer = OpenApi3(
        apiInfo = ApiInfo(
            title = "map uvic",
            version = "0.0.1",
            description = "An API for navigating around the University of Victoria.",
        ),
        json = Jackson,
        servers = listOf(
            ApiServer(
                url = Uri.of("http://localhost:8000"),
                description = "The greatest server!"
            )
        )
    )
    descriptionPath = "/"
    routes += ping()
    routes += search()
}
