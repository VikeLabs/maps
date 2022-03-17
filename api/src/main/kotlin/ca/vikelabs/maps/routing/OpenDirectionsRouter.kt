package ca.vikelabs.maps.routing

import ca.vikelabs.maps.routes.Coordinate
import ca.vikelabs.maps.routing.data.OpenDirectionsOpenDirectionsRouteData
import org.http4k.client.Java8HttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.string

class OpenDirectionsRouter(
    apiKey: String,
    private val httpClient: HttpHandler = Java8HttpClient(),
    private val coordinateToOpenDirectionsSingleLineCoordinate: (Coordinate) -> String = { """${it.latitude},${it.longitude}""" }
) : Router {
    private val key = Query.string().required("key")
    private val from = Query.string().required("from")
    private val to = Query.string().required("to")

    private val baseRequest = Request(Method.GET, "https://open.mapquestapi.com/directions/v2/route")
        .with(key of apiKey)

    override fun route(query: Router.RouteQuery): List<Coordinate> {
        val request = baseRequest.with(
            from of coordinateToOpenDirectionsSingleLineCoordinate(query.from),
            to of coordinateToOpenDirectionsSingleLineCoordinate(query.to)
        )
        val response = httpClient(request)
        val responseBody = OpenDirectionsOpenDirectionsRouteData.lens(response)
        return responseBody.route.locationSequence
            .map { responseBody.route.locations[it] }
            .map { it.latLng }
            .map { Coordinate(it.lat, it.lng) }
    }
}
