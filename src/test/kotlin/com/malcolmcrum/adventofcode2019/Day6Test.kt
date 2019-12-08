package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day6Test {
    @Test
    fun `single planet`() {
        val orbits = parseOrbits("COM)A")

        val count = countOrbits(orbits)

        assertThat(count).isEqualTo(1)
    }

    @Test
    fun `two planets orbiting COM`() {
        val orbits = parseOrbits(
            """
            COM)A
            COM)B
            """.trimIndent()
        )

        val count = countOrbits(orbits)

        assertThat(count).isEqualTo(2)
    }

    @Test
    fun `planet orbiting a planet orbiting COM`() {
        val orbits = parseOrbits(
            """
            COM)A
            A)B
            """.trimIndent()
        )

        val count = countOrbits(orbits)

        assertThat(count).isEqualTo(3)
    }

    @Test
    fun `complex sample`() {
        val orbits = parseOrbits(
            """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
            """.trimIndent()
        )

        val count = countOrbits(orbits)

        assertThat(count).isEqualTo(42)
    }
}