package ca.vikelabs.maps

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.http4k.core.Filter
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import java.util.concurrent.atomic.AtomicBoolean
import javax.sql.DataSource

private val logger = KotlinLogging.logger {}

data class Config(
    val serverPort: Int,
    val dataSource: DataSource,
    val openDirectionsApiKey: String,
) {
    init {
        System.setProperty("org.jooq.no-logo", "true")
        if (isInitialized.getAndSet(true)) {
            throw Exception("Already initialized a Config.")
        }
    }

    companion object {
        private val isInitialized = AtomicBoolean()

        operator fun invoke() = fromEnvironment()

        private fun Map<String, String>.getOrLogAndDefault(key: String, default: String) =
            this[key] ?: run { logger.warn { "No $key found in environment. Defaulting to $default" }; default }

        fun fromEnvironment(): Config {
            val env = System.getenv()
            val serverPort = env.getOrLogAndDefault("SERVER_PORT", "8000").toIntOrNull()
                ?: throw Exception("SERVER_PORT must be an integer.")
            val openDirectionsApiKey =
                env.getOrLogAndDefault("OPEN_DIRECTIONS_API_KEY", "uUwGVDaDn1E7ntjbdgKxLF8blmHRbLdp")

            val hikariConfig = HikariConfig().apply {
                dataSourceClassName =
                    env.getOrLogAndDefault("DATA_SOURCE_CLASS_NAME", "org.postgresql.ds.PGSimpleDataSource")
                username = env.getOrLogAndDefault("DATABASE_USERNAME", "uvic")
                password = env.getOrLogAndDefault("DATABASE_PASSWORD", "uvic")
                dataSourceProperties.apply {
                    setProperty("databaseName", env.getOrLogAndDefault("DATABASE_NAME", "mapuvic"))
                    setProperty("portNumber", env.getOrLogAndDefault("DATABASE_PORT", "5432"))
                    setProperty("serverName", env.getOrLogAndDefault("DATABASE_SERVER_NAME", "localhost"))
                }
            }

            return Config(serverPort, HikariDataSource(hikariConfig), openDirectionsApiKey)
        }
    }

    private val configuredLogger = Filter { next ->
        { req ->
            logger.info { req.toMessage() }
            val response = next(req)
            logger.info { req.toMessage() }
            response
        }
    }

    private val cors = ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive)

    val filters = cors.then(configuredLogger)
}
