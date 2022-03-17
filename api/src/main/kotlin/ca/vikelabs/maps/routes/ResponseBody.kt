package ca.vikelabs.maps.routes

import ca.vikelabs.maps.routing.Router
import ca.vikelabs.maps.routing.route
import org.http4k.contract.meta
import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.double

class Route(private val router: Router) : HttpHandler {
    data class ResponseBody(val points: List<Coordinate>) {
        companion object {
            val lens = Body.auto<ResponseBody>().toLens()
        }
    }

    companion object {
        val fromLatQuery = Query.double().required("fromLatitude")
        val fromLngQuery = Query.double().required("fromLongitude")
        val toLatQuery = Query.double().required("toLatitude")
        val toLngQuery = Query.double().required("toLongitude")
        val spec = "route" meta {
            summary = "Gives a path from `from` to `to`"
            description = "Determines a reasonable but unoptimized route between two points"
            queries += fromLatQuery
            queries += fromLngQuery
            queries += toLatQuery
            queries += toLngQuery
            produces += ContentType.APPLICATION_JSON
            returning(Status.OK, ResponseBody.lens to ResponseBody(listOf(Coordinate(1.0, 2.0), Coordinate(2.0, 2.0))))
        } bindContract Method.GET
    }

    fun route(): HttpHandler = { req ->
        val fromLat = fromLatQuery(req)
        val fromLng = fromLngQuery(req)
        val toLat = toLatQuery(req)
        val toLng = toLngQuery(req)
        val route = router.route(
            fromLat = fromLat,
            fromLng = fromLng,
            toLat = toLat,
            toLng = toLng
        )
        Response(Status.OK).with(
            ResponseBody.lens of ResponseBody(route)
        )
    }

    override fun invoke(request: Request) = contractRoute(request)

    val contractRoute = spec to ::route
}
