package ca.vikelabs.maps.routes

import ca.vikelabs.maps.routes.Ping.Companion.spec
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.ContentType
import org.http4k.core.Status
import org.http4k.hamkrest.hasContentType
import org.http4k.hamkrest.hasStatus
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JsonApprovalTest::class)
class PingTest {
    val ping = Ping()

    @Test
    fun `check status`() {
        assertThat(ping(spec.newRequest()), hasStatus(Status.OK))
    }

    @Test
    fun `check content type`() {
        assertThat(ping(spec.newRequest()), hasContentType(ContentType.APPLICATION_JSON))
    }

    @Test
    fun `check approved`(approver: Approver) {
        approver.assertApproved(ping(spec.newRequest()))
    }
}
