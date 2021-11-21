package ca.vikelabs.maps.data.impl

import ca.vikelabs.maps.util.CachedNetworkTest
import com.natpryce.hamkrest.anyElement
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.throws
import org.http4k.lens.LensFailure
import org.http4k.testing.JsonApprovalTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JsonApprovalTest::class)
class OpenStreetMapsMapDataTest : CachedNetworkTest() {
    @Test
    internal fun `check OpenStreetMapsMapData has default`() {
        assertThat(OpenStreetMapsOverpassMapData(), isA<OpenStreetMapsOverpassMapData>())
    }

    @Test
    internal fun `check buildings successfully parses overpass response`() {
        val mapData = OpenStreetMapsOverpassMapData(client = cachedClient)
        assertThat({ mapData.buildings() }, throws<LensFailure>().not())
    }

    @Test
    internal fun `check elliott is in the buildings`() {
        val mapData = OpenStreetMapsOverpassMapData(client = cachedClient)
        assertThat(mapData.buildings(), anyElement(has("name", { it.name }, equalTo("Elliott Building"))))
    }

    @Test
    internal fun `check buildings does not return duplicates`() {
        val buildings = OpenStreetMapsOverpassMapData(client = cachedClient).buildings()
        assertThat(
            buildings,
            equalTo(buildings.distinctBy { it.name })
        )
    }
}
