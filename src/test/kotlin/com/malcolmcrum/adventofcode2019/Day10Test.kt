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
        val (asteroid, visible) = AsteroidBelt.parse(input).maxVisibleAsteroids()
        assertThat(visible).isEqualTo(8)
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
        val (asteroid, visible) = AsteroidBelt.parse(input).maxVisibleAsteroids()
        assertThat(visible).isEqualTo(210)
    }

    @Test
    fun `vaporization test`() {
        val input = listOf(
            ".#....#####...#..",
            "##...##.#####..##",
            "##...#...#.#####.",
            "..#.....#...###..",
            "..#.#.....#....##"
        )

        val belt = AsteroidBelt.parse(input)
        val (site, _) = belt.maxVisibleAsteroids()
        val vaporized = belt.vaporizeAsteroids(site)

        assertThat(vaporized[0]).isEqualTo(Asteroid(8, 1))
        assertThat(vaporized[1]).isEqualTo(Asteroid(9, 0))
        assertThat(vaporized[2]).isEqualTo(Asteroid(9, 1))
        assertThat(vaporized[8]).isEqualTo(Asteroid(15, 1))
        val asteroidCount = input.joinToString().count { it == '#' }
        assertThat(vaporized.size).isEqualTo(asteroidCount - 1) // exclude our own one
    }

    @Test
    fun `big vaporization test`() {
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
        val belt = AsteroidBelt.parse(input)
        val (asteroid, _) = belt.maxVisibleAsteroids()
        assertThat(asteroid).isEqualTo(Asteroid(11, 13))

        val vaporized = belt.vaporizeAsteroids(asteroid)

        assertThat(vaporized[199]).isEqualTo(Asteroid(8, 2))
    }
}