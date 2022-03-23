package ca.vikelabs.maps

import ca.vikelabs.maps.util.AbstractConfigTest
import ca.vikelabs.maps.util.OpenApiApprovalTest
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.hamkrest.hasStatus
import org.http4k.testing.Approver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(OpenApiApprovalTest::class)
class OpenApiTest : AbstractConfigTest() {
    val application = application(config)

    @Test
    fun `check approved`(approver: Approver) {
        approver.assertApproved(application(Request(GET, "/openapi.json")))
    }

    @Test
    fun `check openapi content type`() {
        assertThat(application(Request(GET, "/openapi.json")), hasStatus(Status.OK))
    }

    @Test
    fun `check openapi status`() {
        assertThat(application(Request(GET, "/openapi.json")), hasStatus(Status.OK))
    }
}
