package ca.vikelabs.maps

import com.fasterxml.jackson.databind.ObjectMapper
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.io.path.Path

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
            Config.fromArgs(
                arrayOf("--config", "src/main/resources/config.json"),
                onFailure = Config.FailureHandlers.throwWithMessage
            )
        }
    }

    @Test
    fun `check config creation from args failure runs onFailure Block`() {
        var ran = false
        Config.fromArgs(arrayOf("--config", "doesNotExist"), onFailure = { ran = true; Config() })
        assertThat(ran, equalTo(true))
    }

    @Test
    internal fun `check create from path`() {
        assertThat(
            { Config.fromPath(Path("src/main/resources/config.json"), Config.FailureHandlers.throwWithMessage) },
            throws<Exception>().not()
        )
    }

    @Test
    internal fun `check create from path throws on invalid path`() {
        assertThat(
            { Config.fromPath(Path("doesNotExist"), Config.FailureHandlers.throwWithMessage) },
            throws<Exception>()
        )
    }
}
