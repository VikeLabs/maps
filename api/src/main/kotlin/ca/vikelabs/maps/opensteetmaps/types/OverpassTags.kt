package ca.vikelabs.maps.opensteetmaps.types

import com.fasterxml.jackson.annotation.JsonProperty

data class OverpassTags(
    @JsonProperty("abbr_name")
    val abbrName: String?,
    @JsonProperty("acronym")
    val acronym: String?,
    @JsonProperty("alt_name")
    val altName: String?,
    @JsonProperty("building")
    val building: String,
    @JsonProperty("building:levels")
    val buildingLevels: String?,
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("old_name")
    val oldName: String?,
    @JsonProperty("source")
    val source: String?,
    @JsonProperty("type")
    val type: String?
)
