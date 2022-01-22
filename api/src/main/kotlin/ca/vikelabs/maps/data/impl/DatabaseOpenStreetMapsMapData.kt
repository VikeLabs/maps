package ca.vikelabs.maps.data.impl

import ca.vikelabs.maps.data.Building
import ca.vikelabs.maps.data.MapData
import ca.vikelabs.maps.domain.Routines.jsonb
import ca.vikelabs.maps.domain.Tables.UVIC_WAY
import ca.vikelabs.maps.domain.tables.UvicArea.UVIC_AREA
import ca.vikelabs.maps.routes.Coordinate
import com.fasterxml.jackson.databind.JsonNode
import org.http4k.format.Jackson
import org.jooq.SQLDialect.POSTGRES
import org.jooq.impl.DSL
import org.jooq.impl.DSL.select
import javax.sql.DataSource

class DatabaseOpenStreetMapsMapData(private val dataSource: DataSource) : MapData {
    override fun buildings(): List<Building> {
        return DSL.using(dataSource, POSTGRES)
            .select(UVIC_AREA.NAME, UVIC_AREA.ABBR_NAME, jsonb(UVIC_AREA.GEOM))
            .from(UVIC_AREA)
            .union(
                select(UVIC_WAY.NAME, UVIC_WAY.ABBR_NAME, jsonb(UVIC_WAY.GEOM))
                    .from(UVIC_WAY)
            )
            .fetch { (name, abbr_name, json) ->
                Building(
                    name,
                    abbr_name,
                    Jackson.mapper.readValue(json.data(), JsonNode::class.java)
                        .get("coordinates")
                        .flattenR()
                        .chunked(2)
                        .map { Coordinate(it[1].asDouble(), it[0].asDouble()) }
                )
            }
    }
}

// flattens nested collections to a single collections recursively.
// Useful for arrays where there are differing levels of nesting
private fun JsonNode.flattenR(): List<JsonNode> {
    if (!isArray) {
        throw Exception("called flattenR on something that was not an array")
    }
    return if (isEmpty) {
        emptyList()
    } else if (get(0).isArray) {
        map { it.flattenR() }.flatten()
    } else {
        toList()
    }
}
