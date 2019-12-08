package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day6Test {
    @Test
    fun `single planet`() {
        val system = SolarSystem.parse(listOf("COM)A"))

        val count = system.countOrbits()

        assertThat(count).isEqualTo(1)
    }

    @Test
    fun `two planets orbiting COM`() {
        val system = SolarSystem.parse(listOf("COM)A", "COM)B"))

        val count = system.countOrbits()

        assertThat(count).isEqualTo(2)
    }

    @Test
    fun `planet orbiting a planet orbiting COM`() {
        val system = SolarSystem.parse(listOf("COM)A", "A)B"))

        val count = system.countOrbits()

        assertThat(count).isEqualTo(3)
    }

    @Test
    fun `complex sample`() {
        val system = SolarSystem.parse(
            listOf(
                "COM)B",
                "B)C",
                "C)D",
                "D)E",
                "E)F",
                "B)G",
                "G)H",
                "D)I",
                "E)J",
                "J)K",
                "K)L"
            )
        )

        val count = system.countOrbits()

        assertThat(count).isEqualTo(42)
    }

    @Test
    fun `distance to santa`() {
        val system = SolarSystem.parse(
            listOf(
                "COM)B",
                "B)C",
                "C)D",
                "D)E",
                "E)F",
                "B)G",
                "G)H",
                "D)I",
                "E)J",
                "J)K",
                "K)L",
                "K)YOU",
                "I)SAN"
            )
        )

        val transfers = system.orbitalTransfersBetween("YOU", "SAN")

        assertThat(transfers).isEqualTo(4)
    }
}