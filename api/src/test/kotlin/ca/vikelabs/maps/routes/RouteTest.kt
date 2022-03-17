package ca.vikelabs.maps.routes

import ca.vikelabs.maps.routes.Route.Companion.fromLatQuery
import ca.vikelabs.maps.routes.Route.Companion.fromLngQuery
import ca.vikelabs.maps.routes.Route.Companion.toLatQuery
import ca.vikelabs.maps.routes.Route.Companion.toLngQuery
import ca.vikelabs.maps.routing.OpenDirectionsRouter
import ca.vikelabs.maps.routing.Router
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.mockk.mockk
import io.mockk.verify
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JsonApprovalTest::class)
internal class RouteTest {
    @Test
    internal fun `check gives 400 when missing query args`() {
        val route = Route(router = { throw Exception("should not be called") })
        val request = Route.spec.newRequest()
        val response = route(request)
        assertThat(response, hasStatus(BAD_REQUEST))
    }

    @Test
    internal fun `check calls router with query args`() {
        val router = mockk<Router>(relaxed = true)
        val route = Route(router)
        val request = Route.spec.newRequest().with(
            fromLatQuery of 1.0,
            fromLngQuery of 2.0,
            toLatQuery of 3.0,
            toLngQuery of 4.0,
        )

        route(request)

        verify(exactly = 1) {
            router.route(
                Router.RouteQuery(
                    from = Coordinate(1.0, 2.0),
                    to = Coordinate(3.0, 4.0)
                )
            )
        }
    }

    @Test
    internal fun `check gives 200 on good request`() {
        val router = mockk<Router>(relaxed = true)
        val route = Route(router)
        val request = Route.spec.newRequest().with(
            fromLatQuery of 1.0,
            fromLngQuery of 2.0,
            toLatQuery of 3.0,
            toLngQuery of 4.0,
        )

        val response = route(request)
        assertThat(response, hasStatus(OK))
    }

    @Test
    internal fun `check returns a Route on 200`() {
        val coordinates = listOf(Coordinate(1.0, 2.0), Coordinate(2.0, 2.0))
        val router = Router { coordinates }
        val route = Route(router)
        val request = Route.spec.newRequest().with(
            fromLatQuery of 1.0,
            fromLngQuery of 2.0,
            toLatQuery of 3.0,
            toLngQuery of 4.0,
        )

        val response = route(request)

        assertThat(response, hasBody(Route.ResponseBody.lens, equalTo(Route.ResponseBody(coordinates))))
    }

    @Test
    internal fun `check route is approved`(approver: Approver) {
        val route = Route(OpenDirectionsRouter("uUwGVDaDn1E7ntjbdgKxLF8blmHRbLdp"))
        val request = Route.spec.newRequest().with(
            fromLatQuery of 48.46382911391982,
            fromLngQuery of -123.30963467575295,
            toLatQuery of 48.46517427947644,
            toLngQuery of -123.31545907142461,
        )
        val response = route(request)
        approver.assertApproved(response)
    }
}
