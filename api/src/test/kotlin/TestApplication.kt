import ca.vikelabs.maps.application
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class TestApplication {
    @Test
    internal fun `test application returns 200 on ping`() {
        val response = application(Request(Method.GET, "/ping"))
        assertThat(response, hasStatus(Status.OK))
    }
}