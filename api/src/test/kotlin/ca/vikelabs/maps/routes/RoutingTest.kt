package ca.vikelabs.maps.routes

import ca.vikelabs.maps.application
import ca.vikelabs.maps.routes.Route.Companion.fromLatQuery
import ca.vikelabs.maps.routes.Route.Companion.fromLonQuery
import ca.vikelabs.maps.routes.Route.Companion.toLatQuery
import ca.vikelabs.maps.routes.Route.Companion.toLonQuery
import ca.vikelabs.maps.util.AbstractConfigTest
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.with
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class RoutingTest : AbstractConfigTest() {
    @Test
    fun `check route exists`() {
        assertThat(application(config)(Request(Method.GET, "/route")), hasStatus(NOT_FOUND).not())
    }

    @Test
    fun `check route accepts query params describing location and goal`() {
        val request = Request(Method.GET, "/route")
            .with(
                toLatQuery of 2.1,
                toLonQuery of 2.1,
                fromLatQuery of 2.1,
                fromLonQuery of 2.1
            )
        assertThat(Route()(request), hasStatus(BAD_REQUEST).not())
    }
}
