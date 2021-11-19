package ca.vikelabs.maps.routes

import ca.vikelabs.maps.application
import ca.vikelabs.maps.data.impl.OpenStreetMapsOverpassMapData
import ca.vikelabs.maps.util.CachedNetworkTest
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.format.Jackson.auto
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasContentType
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class SearchTest : CachedNetworkTest(networkClient = null) {
    private val responseBodyLens = Body.auto<SearchResponseBody>().toLens()
    val searchHandler = search(OpenStreetMapsOverpassMapData(client = cachedClient))

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
            hasBody(responseBodyLens, has("resultsFound", { it.resultsFound }, equalTo(true)))
        )
    }

    @Test
    internal fun `check search for garbage does not have resultsFound`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=ouy3q4fuieafbui213")),
            hasBody(responseBodyLens, has("resultsFound", { it.resultsFound }, equalTo(false)))
        )
    }

    @Test
    internal fun `check search is case insensitive`() {
        assertThat(
            searchHandler(Request(Method.GET, "/search?query=Elliott Building")),
            equalTo(searchHandler(Request(Method.GET, "/search?query=eLlioTT buiLdiNg")))
        )
    }
}
