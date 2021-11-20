package ca.vikelabs.maps.routes

import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.*
import org.http4k.format.Jackson.auto

data class Success(val success: Boolean = true)

fun ping(): ContractRoute {
    val body = Body.auto<Success>().toLens()

    val spec = "/ping" meta {
        summary = "Returns 200 OK and a small json object describing the server status."
        description = "A handy endpoint for checking to see weather the server is alive and replying."
        produces += ContentType.APPLICATION_JSON
        returning(Status.OK, body to Success())
    } bindContract Method.GET

    val ping: HttpHandler = { _ ->
        Response(Status.OK).with(body of Success())
    }

    return spec to ping
}
