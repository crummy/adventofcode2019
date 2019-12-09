package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day7Test {
    @Test
    fun `example 1`() {
        val program = intArrayOf(
            3, 15, // [15] = input
            3, 16, // [16] = input
            1002, 16, 10, 16, // [16] = 10 * [16]
            1, 16, 15, 15, // [15] = [16] + [15]
            4, 15, // output [15]
            99, // end
            0, // [15]
            0 // 16
        )
        val emulator = Emulator2(program)
        val thrust = maxThrusterSignal(emulator)

        assertThat(thrust).isEqualTo(43210)
    }

    @Test
    fun `example 2`() {
        val program =
            intArrayOf(3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23, 101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0)
        val emulator = Emulator2(program)
        val thrust = maxThrusterSignal(emulator)

        assertThat(thrust).isEqualTo(54321)
    }

    @Test
    fun `example 3`() {
        val program =
            intArrayOf(
                3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33, 1002, 33,
                7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0
            )
        val emulator = Emulator2(program)
        val thrust = maxThrusterSignal(emulator)

        assertThat(thrust).isEqualTo(65210)
    }

    @Test
    fun `feedback example 1`() {
        val program =
            intArrayOf(
                3, 26, // [26] = input
                1001, 26, -4, 26,
                3, 27, // [27] = input
                1002, 27, 2, 27,
                1, 27, 26, 27,
                4, 27, // output = [27]
                1001, 28, -1, 28,
                1005, 28, 6,
                99,
                0, // [26]
                0, // [27]
                5
            )
        val thrust = maxThrusterSignalFeedback(program)

        assertThat(thrust).isEqualTo(139629729)
    }
}