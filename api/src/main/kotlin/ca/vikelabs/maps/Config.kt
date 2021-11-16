package ca.vikelabs.maps

import com.fasterxml.jackson.databind.ObjectMapper
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists

data class Config(val port: Int = 8000) {
    companion object {
        private val printMessageThenDefault: (message: String) -> Config = { println(it); Config() }

        fun fromArgs(
            args: Array<String>,
            onFailure: (message: String) -> Config = printMessageThenDefault
        ): Config {
            val configPath = args
                .asSequence()
                .windowed(2, partialWindows = false).map { it[0] to it[1] }
                .find { (key, _) -> key == "--config" }

            return if (configPath != null) {
                fromPath(Path(configPath.second), onFailure)
            } else {
                onFailure("failed to find --config followed by a path")
            }
        }

        private fun fromPath(path: Path, onFailure: (message: String) -> Config = printMessageThenDefault): Config {
            return if (path.exists()) {
                ObjectMapper().readValue(path.toFile(), Config::class.java)
            } else {
                onFailure("$path does not exist")
            }
        }
    }
}
