package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day10Test {
    @Test
    fun `simple test`() {
        val input = listOf(
            ".#..#",
            ".....",
            "#####",
            "....#",
            "...##"
        )
        val max = AsteroidBelt.parse(input).maxVisibleAsteroids()
        assertThat(max).isEqualTo(8)
    }

    @Test
    fun `big test`() {
        val input = listOf(
            ".#..##.###...#######",
            "##.############..##.",
            ".#.######.########.#",
            ".###.#######.####.#.",
            "#####.##.#.##.###.##",
            "..#####..#.#########",
            "####################",
            "#.####....###.#.#.##",
            "##.#################",
            "#####.##.###..####..",
            "..######..##.#######",
            "####.##.####...##..#",
            ".#####..#.######.###",
            "##...#.##########...",
            "#.##########.#######",
            ".####.#.###.###.#.##",
            "....##.##.###..#####",
            ".#.#.###########.###",
            "#.#.#.#####.####.###",
            "###.##.####.##.#..##"
        )
        val max = AsteroidBelt.parse(input).maxVisibleAsteroids()
        assertThat(max).isEqualTo(210)
    }
}