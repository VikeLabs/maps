package ca.vikelabs.maps.routes

import ca.vikelabs.maps.data.Building
import ca.vikelabs.maps.data.MapData
import ca.vikelabs.maps.util.AbstractConfigTest
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasStatus
import org.http4k.testing.Approver
import org.http4k.testing.JsonApprovalTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

internal class SuggestTest {
    @Test
    internal fun `test returns bad request with empty query`() {
        val suggest = Suggest(object : MapData {
            override fun buildings() = emptyList<Building>()
        })

        val response = suggest(
            Suggest.spec.newRequest().with(
                Suggest.query of "",
            )
        )

        assertThat(response, hasStatus(BAD_REQUEST))
    }

    @Test
    internal fun `test returns okay with non empty query`() {
        val suggest = Suggest(object : MapData {
            override fun buildings() = emptyList<Building>()
        })

        val response = suggest(
            Suggest.spec.newRequest().with(
                Suggest.query of "a",
            )
        )

        assertThat(response, hasStatus(OK))
    }

    @Test
    internal fun `check returns buildings starting with e`() {
        val suggest = Suggest(object : MapData {
            override fun buildings() = listOf(Building("Elliot Building", "ELL", bounds = emptyList()))
        })

        val response = suggest(
            Suggest.spec.newRequest().with(
                Suggest.query of "e",
            )
        )

        assertThat(response, hasBody(Suggest.response, equalTo(Suggest.ResponseBody(listOf("ELL", "Elliot Building")))))
    }

    @Nested
    @ExtendWith(JsonApprovalTest::class)
    inner class RealWorldDataTest : AbstractConfigTest() {
        val suggest = Suggest(MapData(config))

        @Test
        internal fun `check suggest works as expected with real data and query of e`(approver: Approver) {
            approver.assertApproved(
                suggest(
                    Suggest.spec.newRequest().with(
                        Suggest.query of "e",
                    )
                )
            )
        }
    }


}
