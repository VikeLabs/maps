import ca.vikelabs.maps.application
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.format.Jackson
import org.http4k.hamkrest.hasContentType
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import kotlin.test.assertTrue

class ApplicationTest {
    @Test
    internal fun `test tests working as expected`() {
        assertTrue(true, "something especially poor has occurred")
    }

    @Test
    internal fun `test swagger returns json documentation`() {
        val response = application(Request(Method.GET, "/swagger.json"))
        assertThat(response, hasStatus(Status.OK))
        assertThat(response, hasContentType(ContentType.APPLICATION_JSON))
    }
}