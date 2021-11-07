package ca.vikelabs.maps.opensteetmaps

import ca.vikelabs.maps.opensteetmaps.types.OverpassResponse
import com.jayway.jsonpath.JsonPath
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.isEmpty
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Status
import org.http4k.format.Jackson.auto
import org.http4k.hamkrest.hasContentType
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

class MapFetcherImplTest {
    private val mapFetcherImpl = MapFetcherImpl()

    @Test
    fun `check get buildings gives 200`() {
        val response = mapFetcherImpl.getBuildings()
        assertThat(response, hasStatus(Status.OK))
    }

    @Test
    fun `check get buildings content type`() {
        val response = mapFetcherImpl.getBuildings()
        assertThat(response, hasContentType(ContentType.APPLICATION_JSON.withNoDirectives()))

    }

    @Test
    fun `check body contents has elements`() {
        val response = mapFetcherImpl.getBuildings()
        assertThat(JsonPath.read<List<Any>>(response.body.stream, "$.elements"), isEmpty.not())

    }

    @Test
    fun `check body is OverpassResponse`() {
        val response = mapFetcherImpl.getBuildings()
        val bodyLens = Body.auto<OverpassResponse>().toLens()
        assertThat(bodyLens(response), isA<OverpassResponse>())
    }
}
