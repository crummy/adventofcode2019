package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class Day2Test {
    @Test
    fun `sample data`() {
        val data = intArrayOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)

        val emulator = Emulator()
        emulator.load(data)
        emulator.run()

        assertThat(emulator.data).isEqualTo(intArrayOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50))
    }

    @ParameterizedTest
    @MethodSource
    fun `small programs`(states: MemoryStates) {
        val emulator = Emulator()
        emulator.load(states.initial)
        emulator.run()

        assertThat(emulator.data).isEqualTo(states.expected)
    }

    companion object {
        @JvmStatic
        fun `small programs`() = listOf(
            MemoryStates(intArrayOf(1, 0, 0, 0, 99), intArrayOf(2, 0, 0, 0, 99)),
            MemoryStates(intArrayOf(2, 3, 0, 3, 99), intArrayOf(2, 3, 0, 6, 99)),
            MemoryStates(intArrayOf(2, 4, 4, 5, 99, 0), intArrayOf(2, 4, 4, 5, 99, 9801)),
            MemoryStates(intArrayOf(1, 1, 1, 4, 99, 5, 6, 0, 99), intArrayOf(30, 1, 1, 4, 2, 5, 6, 0, 99))
        )
    }
}

class MemoryStates(val initial: IntArray, val expected: IntArray)