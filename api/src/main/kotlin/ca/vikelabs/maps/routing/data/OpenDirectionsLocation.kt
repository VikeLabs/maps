package ca.vikelabs.maps.routing.data

data class OpenDirectionsLocation(
    val adminArea1: String,
    val adminArea1Type: String,
    val adminArea3: String,
    val adminArea3Type: String,
    val adminArea4: String,
    val adminArea4Type: String,
    val adminArea5: String,
    val adminArea5Type: String,
    val displayLatLng: OpenDirectionsDisplayLatLng,
    val dragPoint: Boolean,
    val geocodeQuality: String,
    val geocodeQualityCode: String,
    val latLng: OpenDirectionsLatLng,
    val linkId: Int,
    val postalCode: String,
    val sideOfStreet: String,
    val street: String,
    val type: String
)
