package ca.vikelabs.maps.data

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import org.junit.jupiter.api.Test

class MapDataTest {
    @Test
    internal fun `check MapData has default`() {
        assertThat(MapData(), isA<MapData>())
    }
}
