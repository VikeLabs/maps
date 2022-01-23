package ca.vikelabs.maps

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.http4k.core.Filter
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import javax.sql.DataSource

private val logger = KotlinLogging.logger {}


data class Config(
    val serverPort: Int,
    val dataSource: DataSource
) {
    companion object {
        operator fun invoke() = fromEnvironment()

        private fun Map<String, String>.getOrLogAndDefault(key: String, default: String) =
            this[key] ?: run { logger.warn { "No $key found in environment. Defaulting to $default" }; default }

        fun fromEnvironment(): Config {
            val env = System.getenv()
            val serverPort = env.getOrLogAndDefault("SERVER_PORT", "8000").toIntOrNull()
                ?: throw Exception("SERVER_PORT must be an integer.")

            val hikariConfig = HikariConfig()
                .apply {
                    dataSourceClassName =
                        env.getOrLogAndDefault("DATA_SOURCE_CLASS_NAME", "org.postgresql.ds.PGSimpleDataSource")
                    username =
                        env.getOrLogAndDefault("DATABASE_USERNAME", "uvic")
                    password =
                        env.getOrLogAndDefault("DATABASE_PASSWORD", "uvic")
                    dataSourceProperties.apply {
                        setProperty("databaseName", env.getOrLogAndDefault("DATABASE_NAME", "mapuvic"))
                        setProperty("portNumber", env.getOrLogAndDefault("DATABASE_PORT", "5432"))
                        setProperty("serverName", env.getOrLogAndDefault("DATABASE_SERVER_NAME", "localhost"))
                    }
                }

            return Config(serverPort, HikariDataSource(hikariConfig))
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
