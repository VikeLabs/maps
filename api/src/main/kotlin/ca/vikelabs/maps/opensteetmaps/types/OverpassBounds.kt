package ca.vikelabs.maps.opensteetmaps.types


import com.fasterxml.jackson.annotation.JsonProperty

data class OverpassBounds(
    @JsonProperty("maxlat")
    val maxlat: Double,
    @JsonProperty("maxlon")
    val maxlon: Double,
    @JsonProperty("minlat")
    val minlat: Double,
    @JsonProperty("minlon")
    val minlon: Double
)
