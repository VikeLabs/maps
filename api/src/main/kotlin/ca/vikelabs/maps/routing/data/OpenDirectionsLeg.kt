package ca.vikelabs.maps.routing.data

data class OpenDirectionsLeg(
    val destIndex: Int,
    val destNarrative: String,
    val distance: Double,
    val formattedTime: String,
    val hasCountryCross: Boolean,
    val hasFerry: Boolean,
    val hasHighway: Boolean,
    val hasSeasonalClosure: Boolean,
    val hasTollRoad: Boolean,
    val hasUnpaved: Boolean,
    val index: Int,
    val maneuvers: List<OpenDirectionsManeuver>,
    val origIndex: Int,
    val origNarrative: String,
    val roadGradeStrategy: List<List<Any>>,
    val time: Int
)
