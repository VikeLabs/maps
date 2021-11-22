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
class PingTest {
    val ping = ping()

    @Test
    fun `check status`() {

        assertThat(ping(Request(Method.GET, "/ping")), hasStatus(Status.OK))
    }

    @Test
    fun `check content type`() {
        assertThat(ping(Request(Method.GET, "/ping")), hasContentType(ContentType.APPLICATION_JSON))
    }

    @Test
    fun `check approved`(approver: Approver) {
        approver.assertApproved(ping(Request(Method.GET, "/ping")))
    }
}
