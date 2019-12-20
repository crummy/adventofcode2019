package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day19Test {
    @Test
    fun `test box`() {
        val box = Box(985, 1256, 10)

        assertThat(box.topRight).isEqualTo(Pair(995, 1256))
        assertThat(box.bottomLeft).isEqualTo(Pair(985, 1266))
        assertThat(box.topLeft).isEqualTo(Pair(985, 1256))
    }
}