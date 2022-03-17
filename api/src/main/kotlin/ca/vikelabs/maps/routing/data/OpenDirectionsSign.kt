package ca.vikelabs.maps.routing.data

data class OpenDirectionsSign(
    val direction: Int,
    val extraText: String,
    val text: String,
    val type: Int,
    val url: String
)
