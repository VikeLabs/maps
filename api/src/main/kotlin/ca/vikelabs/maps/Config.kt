package ca.vikelabs.maps

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.http4k.core.Filter
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters

private val logger = KotlinLogging.logger {}

data class Config(
    val port: Int = 8000,
    val requestLogging: Boolean = true,
    val responseLogging: Boolean = true,
    val unsafeCors: Boolean = true,
    val database: Database = Database()
) {
    init {
        logger.info {
            """CONFIGURATION:
               port=$port
               requestLogging=$requestLogging
               responseLogging=$responseLogging
               unsafeCors=$unsafeCors
               database=$database
            """.trimIndent()
        }
    }
    data class Database(
        val jdbcUrl: String = "jdbc:postgresql://localhost:5432/mapuvic",
        val password: String = "uvic",
        val username: String = "uvic",
    ) {

        val dataSource by lazy {
            val config = HikariConfig()
            config.jdbcUrl = jdbcUrl
            config.password = password
            config.username = username
            HikariDataSource(config)
        }
    }

    companion object {
        fun fromEnvironment(onFailure: (message: String) -> Config = FailureHandlers.warnAndDefault): Config {
            val env = System.getenv()
            return Config(
                port = env["PORT"]?.toIntOrNull() ?: return onFailure("no PORT found in env"),
                requestLogging = env["REQUEST_LOGGING"]?.equals("true")
                    ?: return onFailure("No REQUEST_LOGGING found in env."),
                responseLogging = env["RESPONSE_LOGGING"]?.equals("true")
                    ?: return onFailure("No RESPONSE_LOGGING found in env."),
                unsafeCors = env["UNSAFE_CORS"]?.equals("true") ?: return onFailure("No UNSAFE_CORS found in env."),
                database = Database(
                    env["DATABASE_JDBC_URL"] ?: return onFailure("No DATABASE_JDBC_URL found in env."),
                    env["DATABASE_USERNAME"] ?: return onFailure("No DATABASE_USERNAME found in env."),
                    env["DATABASE_PASSWORD"] ?: return onFailure("No DATABASE_PASSWORD found in env."),
                )
            )
        }
    }

    object FailureHandlers {
        val warnAndDefault = fun(message: String): Config {
            logger.warn { message }
            logger.warn { "Using default config." }
            return Config()
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
