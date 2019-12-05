package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.containsOnly
import org.junit.jupiter.api.Test

internal class Day5Test {
    @Test
    fun `echo input`() {
        val echoInstructions = intArrayOf(3, 0, 4, 0, 99)
        val emulator = Emulator2(echoInstructions)
        val input = 5
        val output = emulator.run(listOf(input))

        assertThat(output).containsOnly(input)
    }

    @Test
    fun `add values, immediate addressing`() {
        val addInstructions = intArrayOf(
            1101, 2, 3, 0, // [0] = 2 + 3
            4, 0, // read [0]
            99 // end
        )
        val emulator = Emulator2(addInstructions)
        val output = emulator.run()

        assertThat(output).containsOnly(5)
    }

}