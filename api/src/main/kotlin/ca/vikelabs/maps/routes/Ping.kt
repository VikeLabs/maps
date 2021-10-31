package ca.vikelabs.maps.routes

import org.http4k.contract.ContractRoute
import org.http4k.contract.meta
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status


fun ping(): ContractRoute {
    val spec = "/ping" meta {
        summary = "Returns 200 OK and nothing else."
        description = "A handy endpoint for checking to see weather the server is alive and replying"
        returning(Status.OK)
    } bindContract Method.GET

    val ping: HttpHandler = { _ -> Response(Status.OK) }

    return spec to ping
}