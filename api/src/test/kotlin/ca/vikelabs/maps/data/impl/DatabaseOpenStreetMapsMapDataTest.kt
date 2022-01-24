package ca.vikelabs.maps.data.impl

import ca.vikelabs.maps.util.AbstractConfigTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class DatabaseOpenStreetMapsMapDataTest : AbstractConfigTest() {
    @Test
    fun `check converts coordinates correctly`() {
        assertDoesNotThrow {
            DatabaseOpenStreetMapsMapData(config.dataSource).buildings().joinToString("\n") { it.toString() }
                .also { println(it) }
        }
    }
}
