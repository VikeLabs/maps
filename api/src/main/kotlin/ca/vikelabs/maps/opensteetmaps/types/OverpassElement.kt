package ca.vikelabs.maps.opensteetmaps.types

import com.fasterxml.jackson.annotation.JsonProperty

data class OverpassElement(
    @JsonProperty("bounds")
    val bounds: OverpassBounds?,
    @JsonProperty("geometry")
    val geometry: List<OverpassGeometry>?,
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("lat")
    val lat: Double?,
    @JsonProperty("lon")
    val lon: Double?,
    @JsonProperty("members")
    val members: List<OverpassMember>?,
    @JsonProperty("nodes")
    val nodes: List<Long>?,
    @JsonProperty("tags")
    val tags: OverpassTags?,
    @JsonProperty("type")
    val type: String
)
