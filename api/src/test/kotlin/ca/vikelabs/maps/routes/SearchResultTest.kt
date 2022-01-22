package ca.vikelabs.maps.routes

import ca.vikelabs.maps.data.Building
import ca.vikelabs.maps.routes.Search.ResponseBody
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import org.junit.jupiter.api.Test

internal class SearchResultTest {
    @Test
    internal fun `check search result gets correct center`() {
        assertThat(
            ResponseBody.Building(
                Building(
                    "A building", null,
                    listOf(
                        Coordinate(2.0, 2.0),
                        Coordinate(1.0, 2.0),
                        Coordinate(2.0, 1.0),
                        Coordinate(1.0, 1.0)
                    )
                )
            ),
            has("center", { it.center }, equalTo(Coordinate(1.5, 1.5)))
        )
    }
}
