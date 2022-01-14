package ca.vikelabs.maps

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.http4k.core.Filter
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists

private val logger = KotlinLogging.logger {}

data class Config(
    val port: Int = 8000,
    val requestLogging: Boolean = true,
    val responseLogging: Boolean = true,
    val unsafeCors: Boolean = true,
) {
    companion object {
        fun fromArgs(
            args: Array<String>,
            onFailure: (message: String) -> Config = FailureHandlers.warnAndDefault
        ): Config {
            val configPath = args
                .asSequence()
                .windowed(2, partialWindows = false)
                .map { it[0] to it[1] }
                .find { (key, _) -> key == "--config" }

            return if (configPath != null) {
                fromPath(Path(configPath.second), onFailure)
            } else {
                onFailure("Failed to find \"--config\" followed by a path.")
            }
        }

        fun fromPath(
            path: Path,
            onFailure: (message: String) -> Config = FailureHandlers.warnAndDefault
        ): Config {
            return if (path.exists()) {
                ObjectMapper().readValue(path.toFile(), Config::class.java)
            } else {
                onFailure("\"$path\" does not exist")
            }
        }
    }

    object FailureHandlers {
        val warnAndDefault = fun(message: String): Config {
            logger.warn { "$message Using default config." }
            return Config()
        }
        val throwWithMessage = fun(message: String): Nothing {
            throw Exception("Initialization of config failed with: $message")
        }
    }

    private val configuredLogger = Filter { next ->
        { req ->
            if (requestLogging) {
                logger.info { req.toMessage() }
            }
            val response = next(req)
            if (responseLogging) {
                logger.info { req.toMessage() }
            }
            response
        }
    }

    private val cors = if (unsafeCors) ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive) else Filter { it }

    val filters = cors.then(configuredLogger)
}
