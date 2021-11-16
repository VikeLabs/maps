package ca.vikelabs.maps.opensteetmaps.types

import com.fasterxml.jackson.annotation.JsonProperty

data class OverpassResponse(
    @JsonProperty("elements")
    val elements: List<OverpassElement>,
    @JsonProperty("generator")
    val generator: String,
    @JsonProperty("osm3s")
    val osm3s: OverpassOsm3s,
    @JsonProperty("version")
    val version: Double
)
