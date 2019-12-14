package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day12Test {
    @Test
    fun `sample test`() {
        val moons = listOf(
            Moon(-1, 0, 2),
            Moon(2, -10, -7),
            Moon(4, -8, 8),
            Moon(3, 5, -1)
        )

        repeat(10) { moons.simulateMotion() }

        assertThat(moons.map { it.pos }).containsOnly(
            Vector3(2, 1, -3),
            Vector3(1, -8, 0),
            Vector3(3, -6, 1),
            Vector3(2, 0, 4)
        )
        assertThat(moons.map { it.vel }).containsOnly(
            Vector3(-3, -2, 1),
            Vector3(-1, 1, 3),
            Vector3(3, 2, -3),
            Vector3(1, -1, -1)
        )

        assertThat(moons.totalEnergy()).isEqualTo(179)
    }

    @Test
    fun `find another identical state`() {
        val moons = listOf(
            Moon(-1, 0, 2),
            Moon(2, -10, -7),
            Moon(4, -8, 8),
            Moon(3, 5, -1)
        )

        val count = moons.simulationsUntilRepeat()

        assertThat(count).isEqualTo(2772)
    }
}
