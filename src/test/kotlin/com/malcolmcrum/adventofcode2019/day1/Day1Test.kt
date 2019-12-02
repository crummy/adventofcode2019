package com.malcolmcrum.adventofcode2019.day1

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class Day1Test {
    @ParameterizedTest
    @MethodSource
    fun `calculate fuel required`(values: FuelTest) {
        val mass = values.mass
        val fuel = values.expectedFuel
        assertThat(fuelCounterUpper(mass)).isEqualTo(fuel)
    }

    @Test
    fun `verify math in reverse`() {
        (0..100).forEach {
            val fuel = it
            val mass = (fuel + 2) * 3
            assertThat(fuel).isEqualTo(fuelCounterUpper(mass))
        }
    }

    companion object {
        @JvmStatic
        fun `calculate fuel required`() = listOf(
            FuelTest(12, 2),
            FuelTest(14, 2),
            FuelTest(1969, 654),
            FuelTest(100756, 33583)
        ).plus(
            listOf(
                FuelTest(11, 1),
                FuelTest(15, 3)
            )
        )
    }
}

data class FuelTest(val mass: Int, val expectedFuel: Int)