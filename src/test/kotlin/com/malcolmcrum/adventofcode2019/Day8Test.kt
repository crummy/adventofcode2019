package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day8Test {
    @Test
    fun `simple decoding`() {
        val layers = decode("123456789012", 3, 2)

        assertThat(layers[0]).isEqualTo(listOf(1, 2, 3, 4, 5, 6))
        assertThat(layers[1]).isEqualTo(listOf(7, 8, 9, 0, 1, 2))
    }

    @Test
    fun `simple rendering`() {
        val layers = decode("0222112222120000", 2, 2)
        val output = render(layers, 2, 2)

        assertThat(output).isEqualTo(listOf(0, 1, 1, 0))

        val bitmap = output.bitmap(2)

        println(bitmap)
    }
}