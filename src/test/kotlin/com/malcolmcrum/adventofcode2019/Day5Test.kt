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
            4, 0, // output [0]
            99 // end
        )
        val emulator = Emulator2(addInstructions)
        val output = emulator.run()

        assertThat(output).containsOnly(5)
    }

    @Test
    fun `position mode, check equal to 8`() {
        val equals8Instructions = intArrayOf(
            3, 9, // [9] = input
            8, 9, 10, 9, // [9] = [9] == [10]
            4, 9, // output [9]
            99, // end
            -1, // [9] - result is stored here
            8 // [10] - comparison value
        )
        val emulator = Emulator2(equals8Instructions)
        assertThat(emulator.run(listOf(8))).containsOnly(1)
        assertThat(emulator.run(listOf(1))).containsOnly(0)
    }

    @Test
    fun `position mode, check less than 8`() {
        val lessThan8Instructions = intArrayOf(
            3, 9, // [9] = input
            7, 9, 10, 9, // [9] = [9] < [10]
            4, 9, // output [9]
            99, // end
            -1, // [9] - result is stored here
            8 // [10] - comparison value
        )
        val emulator = Emulator2(lessThan8Instructions)
        assertThat(emulator.run(listOf(8))).containsOnly(0)
        assertThat(emulator.run(listOf(7))).containsOnly(1)
    }

    @Test
    fun `immediate mode, check less than 8`() {
        val lessThan8Instructions = intArrayOf(
            3, 3, // [9] = input
            1107, -1, 8, 3, // [3] = -1 < 8
            4, 3, // output [3]
            99 // end
        )
        val emulator = Emulator2(lessThan8Instructions)
        assertThat(emulator.run(listOf(8))).containsOnly(0)
        assertThat(emulator.run(listOf(7))).containsOnly(1)
    }

    @Test
    fun `large example`() {
        val instructions = intArrayOf(
            3, 21, // [21] = input
            1008, 21, 8, 20, // [20] = [21] == 8
            1005, 20, 22, // if [20] != 0 jump to [22]
            107, 8, 21, 20, 1006, 20, 31,
            1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
            999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        )
        val emulator = Emulator2(instructions)

        assertThat(emulator.run(listOf(5))).containsOnly(999)
        assertThat(emulator.run(listOf(8))).containsOnly(1000)
        assertThat(emulator.run(listOf(55))).containsOnly(1001)
    }

    @Test
    fun `jump test 1`() {
        val jumpTest = intArrayOf(
            3, 12, // [12] = input
            6, 12, 15, // if [12] != 0 jump to [15] (9)
            1, 13, 14, 13, // [13] = [13] + [14]
            4, 13, // output [13] (0)
            99, // end
            -1, // [12]
            0, // [13]
            1, // [14]
            9 // [15]
        )
        val emulator = Emulator2(jumpTest)
        assertThat(emulator.run(listOf(0))).containsOnly(0)
        assertThat(emulator.run(listOf(1))).containsOnly(1)
        assertThat(emulator.run(listOf(2))).containsOnly(1)
    }

}