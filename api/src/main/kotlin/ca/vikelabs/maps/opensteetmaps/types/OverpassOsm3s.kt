package ca.vikelabs.maps.opensteetmaps.types


import com.fasterxml.jackson.annotation.JsonProperty

data class OverpassOsm3s(
    @JsonProperty("copyright")
    val copyright: String,
    @JsonProperty("timestamp_areas_base")
    val timestampAreasBase: String,
    @JsonProperty("timestamp_osm_base")
    val timestampOsmBase: String
)
