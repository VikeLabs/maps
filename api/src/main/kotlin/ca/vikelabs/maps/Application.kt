package ca.vikelabs.maps

import ca.vikelabs.maps.data.impl.DatabaseOpenStreetMapsMapData
import ca.vikelabs.maps.routes.Route
import ca.vikelabs.maps.routes.Search
import ca.vikelabs.maps.routes.Ping
import mu.KotlinLogging
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.ApiRenderer
import org.http4k.contract.openapi.OpenAPIJackson
import org.http4k.contract.openapi.v3.ApiServer
import org.http4k.contract.openapi.v3.AutoJsonToJsonSchema
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.HttpHandler
import org.http4k.core.Uri

private val logger = KotlinLogging.logger {}

private val warnAndDefaultConfig by lazy {
    logger.info { "No configuration given to application. Using default config" }
    Config()
}

fun application(
    config: Config = warnAndDefaultConfig
): HttpHandler {

    return contract {
        renderer = OpenApi3(
            apiInfo = ApiInfo(
                title = "map uvic",
                version = "0.0.1",
                description = "An API for navigating around the University of Victoria.",
            ),
            json = OpenAPIJackson,
            apiRenderer = ApiRenderer.Auto(
                json = OpenAPIJackson,
                schema = AutoJsonToJsonSchema(
                    OpenAPIJackson,
                    modelNamer = { "${it.javaClass.enclosingClass?.simpleName ?: ""}${it.javaClass.simpleName}" }
                )
            ),
            servers = listOf(
                ApiServer(
                    url = Uri.of("http://localhost:${config.port}"),
                    description = "The greatest server!"
                )
            )
        )
        descriptionPath = "/"
        routes += Ping().contractRoute
        routes += Search(DatabaseOpenStreetMapsMapData(config.database.dataSource)).contractRoute
        routes += Route().contractRoute
    }
}
