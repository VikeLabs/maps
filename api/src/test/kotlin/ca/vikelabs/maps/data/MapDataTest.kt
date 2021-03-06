package ca.vikelabs.maps.data

import ca.vikelabs.maps.util.AbstractConfigTest
import com.natpryce.hamkrest.anyElement
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.throws
import org.http4k.lens.LensFailure
import org.junit.jupiter.api.Test

class MapDataTest : AbstractConfigTest() {
    @Test
    internal fun `check MapData has default`() {
        assertThat(MapData(config), isA<MapData>())
    }

    @Test
    internal fun `check buildings successfully parses overpass response`() {
        val mapData = MapData(config)
        assertThat({ mapData.buildings() }, throws<LensFailure>().not())
    }

    @Test
    internal fun `check elliott is in the buildings`() {
        val mapData = MapData(config)
        assertThat(mapData.buildings(), anyElement(has("name", { it.name }, equalTo("Elliott Building"))))
    }

    @Test
    internal fun `check buildings does not return duplicates`() {
        val buildings = MapData(config).buildings()
        assertThat(
            buildings,
            equalTo(buildings.distinct())
        )
    }
}
