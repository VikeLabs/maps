package ca.vikelabs.maps

import ca.vikelabs.maps.data.impl.OpenStreetMapsOverpassMapData
import ca.vikelabs.maps.routes.ping
import ca.vikelabs.maps.routes.route
import ca.vikelabs.maps.routes.search
import mu.KotlinLogging
import org.http4k.client.JavaHttpClient
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.ApiServer
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.TrafficFilters
import org.http4k.format.Jackson
import org.http4k.traffic.ReadWriteCache

private val logger = KotlinLogging.logger {}

private val warnAndDefaultConfig by lazy {
    logger.info { "No configuration given to application. Using default config" }
    Config()
}

private val cachingJavaHttpClient = TrafficFilters.RecordTo(ReadWriteCache.Disk("cache"))
    .then(TrafficFilters.ServeCachedFrom(ReadWriteCache.Disk("cache")))
    .then(JavaHttpClient())

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
            json = Jackson,
            servers = listOf(
                ApiServer(
                    url = Uri.of("http://localhost:${config.port}"),
                    description = "The greatest server!"
                )
            )
        )
        descriptionPath = "/"
        routes += ping()
        routes += search(mapsData = OpenStreetMapsOverpassMapData(cachingJavaHttpClient))
        routes += route()
    }
}
