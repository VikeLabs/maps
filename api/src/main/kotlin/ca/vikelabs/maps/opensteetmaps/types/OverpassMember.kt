package ca.vikelabs.maps.opensteetmaps.types


import com.fasterxml.jackson.annotation.JsonProperty

data class OverpassMember(
    @JsonProperty("geometry")
    val geometry: List<OverpassGeometry>,
    @JsonProperty("ref")
    val ref: Int,
    @JsonProperty("role")
    val role: String,
    @JsonProperty("type")
    val type: String?
)
