import ca.vikelabs.maps.ping
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.hamkrest.hasContentType
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class ApplicationTest {
    @Test
    internal fun `test swagger returns json documentation`() {
        val response = ping(Request(Method.GET, "/swagger.json"))
        assertThat(response, hasStatus(Status.OK))
        assertThat(response, hasContentType(ContentType.APPLICATION_JSON))
    }
}