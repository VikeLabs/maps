package ca.vikelabs.maps

import mu.KotlinLogging
import org.http4k.server.Jetty
import org.http4k.server.SunHttp
import org.http4k.server.asServer

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    val configuration = Config.fromArgs(args)
    application(configuration)
        .asServer(Jetty(configuration.port))
        .start()
        .also { logger.info { "Maps server started! Running on: http://localhost:${it.port()}" } }
        .block()
}
