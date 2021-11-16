package ca.vikelabs.maps

import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    val server = application.asServer(SunHttp())
    server.start()
    println("Server started and running on http://localhost:${server.port()}.")
    server.block()
}
