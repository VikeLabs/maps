package ca.vikelabs.maps.data

import ca.vikelabs.maps.data.impl.OpenStreetMapsOverpassMapData

interface MapData {
    fun buildings(): List<Building>

    companion object {
        operator fun invoke(): MapData = OpenStreetMapsOverpassMapData()
    }
}

data class Building(val name: String)
