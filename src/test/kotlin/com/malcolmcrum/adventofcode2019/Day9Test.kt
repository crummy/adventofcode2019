package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import org.junit.jupiter.api.Test

internal class Day9Test {
    @Test
    fun `create copy of self`() {
        val program = longArrayOf(
            109, 1, // relative base = 1
            204, -1, // output +=
            1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99)
        val output = Emulator4("echo", program, listOf()).run()

        assertThat(output).isEqualTo(program.toList())
    }

    @Test
    fun `output number`() {
        val program = longArrayOf(104, 1125899906842624, 99)
        val output = Emulator4("echo", program, listOf()).run()

        assertThat(output).containsExactly(1125899906842624)
    }

    @Test
    fun `output 16-digit number`() {
        val program = longArrayOf(1102, 34915192, 34915192, 7, 4, 7, 99, 0)
        val output = Emulator4("echo", program, listOf()).run()

        assertThat(output[0]).isGreaterThan(999999999999999)
    }
}