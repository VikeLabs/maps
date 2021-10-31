package ca.vikelabs.maps.routes

import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.hamkrest.hasContentType
import org.http4k.hamkrest.hasStatus
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JsonApprovalTest::class)
internal class PingTest {
    private val pingRoute = ping()
    private val request = Request(Method.GET, "/ping")
    private val response = pingRoute(request)

    @Test
    fun `check status`() {
        assertThat(response, hasStatus(Status.OK))
    }

    @Test
    fun `check content type`() {
        assertThat(response, hasContentType(ContentType.APPLICATION_JSON))
    }

    @Test
    fun `check approved`(approver: Approver) {
        approver.assertApproved(response)
    }
}