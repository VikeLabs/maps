package ca.vikelabs.maps

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class ConfigTest {
    @Test
    fun `check default config is the same as the checked in config`() {
        val checkedInFile = Config::class.java.classLoader.getResourceAsStream("config.json")
        val checkedInConfig = ObjectMapper().readValue(checkedInFile, Config::class.java)
        assertThat(checkedInConfig, equalTo(Config()))
    }

    @Test
    fun `check config creation from args works as expected`() {
        assertDoesNotThrow {
            Config.fromArgs(arrayOf("--config", "src/main/resources/config.json"), onFailure = { throw Exception(it) })
        }
    }

    @Test
    fun `check config creation from args failure runs onFailure Block`() {
        var ran = false
        Config.fromArgs(arrayOf("--config", "doesNotExist"), onFailure = { ran = true; Config() })
        assertThat(ran, equalTo(true))
    }
}
