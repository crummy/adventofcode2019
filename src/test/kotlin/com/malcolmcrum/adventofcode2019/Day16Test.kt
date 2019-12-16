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
}