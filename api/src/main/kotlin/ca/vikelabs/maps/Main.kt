package ca.vikelabs.maps

import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main(args: Array<String>) {
    val configuration = Config.fromArgs(args)
    val server = application(configuration).asServer(SunHttp(configuration.port))
    server.start()
    println(
        """
        |Maps server started!
        |Running on:
        |   - http://localhost:${server.port()}
        |""".trimMargin()
    )
    server.block()
}
