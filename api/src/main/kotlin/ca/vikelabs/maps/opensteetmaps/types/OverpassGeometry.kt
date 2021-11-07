package ca.vikelabs.maps.opensteetmaps.types


import com.fasterxml.jackson.annotation.JsonProperty

data class OverpassGeometry(
    @JsonProperty("lat")
    val lat: Double,
    @JsonProperty("lon")
    val lon: Double
)
