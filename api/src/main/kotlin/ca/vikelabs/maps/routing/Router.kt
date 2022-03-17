package ca.vikelabs.maps.routing

import ca.vikelabs.maps.routes.Coordinate

fun interface Router {
    data class RouteQuery(val from: Coordinate, val to: Coordinate)

    fun route(query: RouteQuery): List<Coordinate>
}

fun Router.route(from: Coordinate, to: Coordinate) = route(Router.RouteQuery(from, to))
fun Router.route(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double) =
    route(from = Coordinate(fromLat, fromLng), to = Coordinate(toLat, toLng))

