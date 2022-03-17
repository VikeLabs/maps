package ca.vikelabs.maps

import ca.vikelabs.maps.data.impl.DatabaseOpenStreetMapsMapData
import ca.vikelabs.maps.routes.Ping
import ca.vikelabs.maps.routes.Route
import ca.vikelabs.maps.routes.Search
import ca.vikelabs.maps.routes.Suggest
import ca.vikelabs.maps.routing.Router
import org.http4k.contract.ContractRoutingHttpHandler
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.ApiRenderer
import org.http4k.contract.openapi.OpenAPIJackson
import org.http4k.contract.openapi.v3.ApiServer
import org.http4k.contract.openapi.v3.AutoJsonToJsonSchema
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.Uri

fun application(config: Config): ContractRoutingHttpHandler {
    val mapData = DatabaseOpenStreetMapsMapData(config.dataSource)
    val router = Router { TODO() }
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
                    modelNamer = { appendEnclosingClass(it.javaClass) }
                )
            ),
            servers = listOf(
                ApiServer(
                    url = Uri.of("http://localhost:${config.serverPort}"),
                    description = "The greatest server!"
                )
            )
        )
        descriptionPath = "/openapi.json"
        routes += Ping().contractRoute
        routes += Search(mapData).contractRoute
        routes += Suggest(mapData).contractRoute
        routes += Route(router).contractRoute
    }
}


private fun appendEnclosingClass(clazz: Class<*>): String =
    if (clazz.enclosingClass != null) appendEnclosingClass(clazz.enclosingClass) + clazz.simpleName
    else clazz.simpleName
