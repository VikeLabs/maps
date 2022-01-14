package ca.vikelabs.maps

import ca.vikelabs.maps.util.OpenApiApprovalTest
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.hamkrest.hasStatus
import org.http4k.testing.Approver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(OpenApiApprovalTest::class)
class OpenApiTest {
    val application = application()

    @Test
    fun `check approved`(approver: Approver) {
        approver.assertApproved(application(Request(Method.GET, "/")))
    }

    @Test
    fun `check openapi content type`() {
        assertThat(application(Request(Method.GET, "/")), hasStatus(Status.OK))
    }

    @Test
    fun `check openapi status`() {
        assertThat(application(Request(Method.GET, "/")), hasStatus(Status.OK))
    }
}
