package ca.vikelabs.maps

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.SunHttp
import org.http4k.server.asServer

val application: HttpHandler = { _: Request ->
    Response(Status.OK)
}

fun main() {
    val server = application.asServer(SunHttp())
    server.start()
    println("server started and running on ${server.port()}")
    server.block()
}