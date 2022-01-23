package ca.vikelabs.maps.data

import ca.vikelabs.maps.Config
import ca.vikelabs.maps.data.impl.DatabaseOpenStreetMapsMapData
import ca.vikelabs.maps.routes.Coordinate

interface MapData {
    fun buildings(): List<Building>

    companion object {
        operator fun invoke(config: Config = Config()): MapData =
            DatabaseOpenStreetMapsMapData(config.database.dataSource)
    }
}

data class Building(val name: String, val abbrName: String?, val bounds: List<Coordinate>) {
    val center by lazy { bounds.reduce { acc, coordinates -> acc + coordinates } / bounds.size }
}
