package ca.vikelabs.maps.routes

import ca.vikelabs.maps.application
import ca.vikelabs.maps.data.MapData
import ca.vikelabs.maps.util.AbstractConfigTest
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.anyElement
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThanOrEqualTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.hasSize
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasContentType
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class SearchTest : AbstractConfigTest() {
    val searchHandler = Search(MapData(config))
    val application = application(config)

    @Test
    internal fun `check search exists`() {
        assertThat(
            application(Request(Method.GET, "/search")),
            hasStatus(Status.NOT_FOUND).not()
        )
    }

    @Test
    internal fun `check searching for Elliot Building returns 200`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=Elliott Building")),
            hasStatus(Status.OK)
        )
    }

    @Test
    internal fun `check search returns json`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=Elliott Building")),
            hasContentType(ContentType.APPLICATION_JSON)
        )
    }

    @Test
    internal fun `check search for Elliott Building has results found`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=Elliott Building")),
            hasBody(Search.response, has("results size", { it.results.size }, greaterThanOrEqualTo(1)))
        )
    }

    @Test
    internal fun `check search for garbage does not have resultsFound`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=ouy3q4fuieafbui213")),
            hasBody(Search.response, has("results size", { it.results.size }, equalTo(0)))
        )
    }

    @Test
    internal fun `check search is case insensitive`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=Elliott Building")),
            equalTo(searchHandler(Request(Method.GET, "/search?query=eLlioTT buiLdiNg")))
        )
    }

    @Test
    internal fun `check search works with name levenshtein distance of one`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=Elliott Buiding")),
            equalTo(searchHandler(Request(Method.GET, "/search?query=eLlioTT buiLdiNg")))
        )
    }

    @Test
    internal fun `check search works with exact abbr_name`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=CSR")),
            hasBody(
                Search.response,
                has("results", { it.results }, hasSize(greaterThanOrEqualTo(1)))
            )
        )
    }

    @Test
    internal fun `check searching elliot yields the Elliot Building`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=elliott")),
            hasBody(
                Search.response,
                has("results", { it.results }, anyElement(has("name", { it.name }, equalTo("Elliott Building"))))
            )
        )
    }

    @Test
    internal fun `check searching elliot and yeet yields the Elliot Building`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=elliott and yeet")),
            hasBody(
                Search.response,
                has("results", { it.results }, anyElement(has("name", { it.name }, equalTo("Elliott Building"))))
            )
        )
    }

    @Test
    fun `check returns reasonable centers`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=elliott")),
            hasBody(
                Search.response,
                has("results", { it.results },
                    anyElement(has("name", { it.center },
                        Matcher("near 48,123")
                        { it.latitude - 48 < 5 && it.longitude - (-123) < 5 }
                    ))
                )
            )
        )
    }

    @Test
    fun `check searching for empty string is bad request`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=")),
            hasStatus(BAD_REQUEST)
        )
    }
}
