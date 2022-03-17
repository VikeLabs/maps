package ca.vikelabs.maps.routing.data

data class OpenDirectionsManeuver(
    val attributes: Int,
    val direction: Int,
    val directionName: String,
    val distance: Double,
    val formattedTime: String,
    val iconUrl: String,
    val index: Int,
    val linkIds: List<Any>,
    val maneuverNotes: List<Any>,
    val mapUrl: String?,
    val narrative: String,
    val signs: List<OpenDirectionsSign>,
    val startPoint: OpenDirectionsStartPoint,
    val streets: List<String>,
    val time: Int,
    val transportMode: String,
    val turnType: Int
)
