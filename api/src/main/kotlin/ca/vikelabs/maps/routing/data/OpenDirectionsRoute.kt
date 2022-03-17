package ca.vikelabs.maps.routing.data

data class OpenDirectionsRoute(
    val boundingBox: OpenDirectionsBoundingBox,
    val computedWaypoints: List<Any>,
    val distance: Double,
    val formattedTime: String,
    val fuelUsed: Double,
    val hasCountryCross: Boolean,
    val hasFerry: Boolean,
    val hasHighway: Boolean,
    val hasSeasonalClosure: Boolean,
    val hasTollRoad: Boolean,
    val hasUnpaved: Boolean,
    val legs: List<OpenDirectionsLeg>,
    val locationSequence: List<Int>,
    val locations: List<OpenDirectionsLocation>,
    val options: OpenDirectionsOptions,
    val realTime: Int,
    val routeError: OpenDirectionsRouteError,
    val sessionId: String,
    val time: Int
)
