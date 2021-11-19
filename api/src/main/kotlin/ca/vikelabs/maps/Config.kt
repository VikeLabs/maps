package ca.vikelabs.maps

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists

private val logger = KotlinLogging.logger {}

data class Config(val port: Int = 8000) {

    object FailureHandlers {
        val getLogAndDefault = fun(message: String): Config {
            logger.warn { message }
            logger.warn { "Using default config." }
            return Config()
        }
        val throwWithMessage = fun(it: String): Nothing {
            throw Exception("initialization of config failed with: $it")
        }
    }

    companion object {
        fun fromArgs(
            args: Array<String>,
            onFailure: (message: String) -> Config = FailureHandlers.getLogAndDefault
        ): Config {
            val configPath = args
                .asSequence()
                .windowed(2, partialWindows = false).map { it[0] to it[1] }
                .find { (key, _) -> key == "--config" }

            return if (configPath != null) {
                fromPath(Path(configPath.second), onFailure)
            } else {
                onFailure("Failed to find \"--config\" followed by a path.")
            }
        }

        fun fromPath(
            path: Path,
            onFailure: (message: String) -> Config = FailureHandlers.getLogAndDefault
        ): Config {
            return if (path.exists()) {
                ObjectMapper().readValue(path.toFile(), Config::class.java)
            } else {
                onFailure("\"$path\" does not exist")
            }
        }
    }
}
