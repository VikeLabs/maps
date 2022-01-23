package ca.vikelabs.maps.routes

import ca.vikelabs.maps.routes.Ping.ResponseBody.Companion.lens
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto

class Ping : HttpHandler {
    data class ResponseBody(val success: Boolean = true) {
        companion object {
            val lens = Body.auto<ResponseBody>().toLens()
        }
    }

    companion object {
        val spec = "/ping" meta {
            summary = "Returns 200 OK and a small json object describing the server status."
            description = "A handy endpoint for checking to see weather the server is alive and replying."
            produces += ContentType.APPLICATION_JSON
            returning(Status.OK, lens to ResponseBody(), "a successful ping!")
        } bindContract Method.GET
    }

    private fun ping(): HttpHandler = { _ ->
        Response(Status.OK).with(lens of ResponseBody())
    }

    val contractRoute = spec to ::ping
    override fun invoke(request: Request) = contractRoute(request)
}
