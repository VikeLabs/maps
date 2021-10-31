import ca.vikelabs.maps.application
import org.http4k.core.Method
import org.http4k.core.Request
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
}