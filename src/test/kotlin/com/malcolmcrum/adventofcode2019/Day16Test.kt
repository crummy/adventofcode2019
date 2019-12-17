package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.startsWith
import org.junit.jupiter.api.Test

internal class Day16Test {

    @Test
    fun `simple test`() {
        val output = fft("12345678")

        assertThat(output).isEqualTo("48226158")

        val after4phases = fft("12345678", 4)

        assertThat(after4phases).isEqualTo("01029498")
    }

    @Test
    fun `larger samples`() {
        assertThat(fft("80871224585914546619083218645595", 100)).startsWith("24176176")
        assertThat(fft("19617804207202209144916044189917", 100)).startsWith("73745418")
        assertThat(fft("69317163492948606335995924319873", 100)).startsWith("52432133")
    }

    @Test
    fun `test multiplier`() {
        repeat(8) { iteration ->
            val pattern = pattern(16, iteration)
            repeat(16) { digit ->
                assertThat(multiplier(digit, iteration)).isEqualTo(pattern[digit])
            }
        }
    }

    @Test
    fun `test selecting just part of a range`() {
        assertThat(findMessage("03036732577212944063491565474664")).isEqualTo("84462026")
        assertThat(findMessage("02935109699940807407585447034323")).isEqualTo("78725270")
        assertThat(findMessage("03081770884921959731165446850517")).isEqualTo("53553731")
    }

    // known good, but inefficient, pattern generation
    fun pattern(length: Int, iteration: Int): List<Int> {
        val pattern = mutableListOf<Int>()
        do {
            repeat(iteration + 1) { pattern += 0 }
            repeat(iteration + 1) { pattern += 1 }
            repeat(iteration + 1) { pattern += 0 }
            repeat(iteration + 1) { pattern += -1 }
        } while (pattern.size < length + 1) // +1 to offset the skip 1 on the next line
        return pattern.subList(1, length + 1)
    }
}