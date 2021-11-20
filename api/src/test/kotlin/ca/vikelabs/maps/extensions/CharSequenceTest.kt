package ca.vikelabs.maps.extensions

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class CharSequenceTest {
    @Test
    internal fun `check levenshteinDistance with same strings is 0`() {
        assertThat("hello".levenshteinDistanceTo("hello"), equalTo(0))
    }

    @Test
    internal fun `check levenshteinDistance with string missing one char is 1`() {
        assertThat("hello".levenshteinDistanceTo("hell"), equalTo(1))
    }

    @Test
    internal fun `check levenshteinDistance with one swapped char is 1`() {
        assertThat("hello".levenshteinDistanceTo("hollo"), equalTo(1))
    }
}
