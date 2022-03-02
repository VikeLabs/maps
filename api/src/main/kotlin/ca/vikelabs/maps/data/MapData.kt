package ca.vikelabs.maps.data

import ca.vikelabs.maps.Config
import ca.vikelabs.maps.data.impl.DatabaseOpenStreetMapsMapData
import ca.vikelabs.maps.routes.Coordinate

interface MapData {
    fun buildings(): List<Building>

    companion object {
        operator fun invoke(config: Config): MapData =
            DatabaseOpenStreetMapsMapData(config.dataSource)
    }
}

data class Building(val name: String, val abbrName: String?, val bounds: List<Coordinate>) {
    val center by lazy { bounds.mean() }
}

private fun List<Coordinate>.mean() = mean(Coordinate::plus, Coordinate::div)

private fun <T> List<T>.mean(add: (T, T) -> T, div: (T, Int) -> T) = div(reduce(add), size)

