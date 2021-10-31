package ca.vikelabs.maps.routes

import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

internal class PingTest {
    private val pingRoute = ping()

    @Test
    internal fun `test ping returns 200`() {
        val response = pingRoute(Request(Method.GET, "/ping"))
        assertThat(response, hasStatus(Status.OK))
    }

    @Test
    internal fun `test ping returns no body`() {
        val response = pingRoute(Request(Method.GET, "/ping"))
        assertThat(response, hasBody(""))
    }
}