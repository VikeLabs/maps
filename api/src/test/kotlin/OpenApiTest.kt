import ca.vikelabs.maps.application
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.hamkrest.hasStatus
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JsonApprovalTest::class)
class OpenApiTest {

    @Test
    fun `check approved`(approver: Approver) {
        approver.assertApproved(application(Request(Method.GET, "/swagger.json")))
    }

    @Test
    fun `check swagger content type`() {
        assertThat(application(Request(Method.GET, "/swagger.json")), hasStatus(Status.OK))
    }

    @Test
    fun `check swagger status`() {
        assertThat(application(Request(Method.GET, "/swagger.json")), hasStatus(Status.OK))
    }
}