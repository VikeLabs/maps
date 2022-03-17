package ca.vikelabs.maps.routing.data

data class OpenDirectionsInfo(
    val copyright: OpenDirectionsCopyright,
    val messages: List<Any>,
    val statuscode: Int
)
