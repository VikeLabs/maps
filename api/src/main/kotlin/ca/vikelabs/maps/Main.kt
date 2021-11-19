package ca.vikelabs.maps

import mu.KotlinLogging
import org.http4k.server.SunHttp
import org.http4k.server.asServer

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    val configuration = Config.fromArgs(args)
    val server = application(configuration).asServer(SunHttp(configuration.port))
    server.start()
    logger.info { "Maps server started!" }
    logger.info { "Running on: http://localhost:${server.port()}" }
    server.block()
}
