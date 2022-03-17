package ca.vikelabs.maps.routing.data

import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.core.Body

data class OpenDirectionsOpenDirectionsRouteData(
    val info: OpenDirectionsInfo,
    val route: OpenDirectionsRoute
) {
    companion object {
        val lens = Body.auto<OpenDirectionsOpenDirectionsRouteData>().toLens()
    }
}
