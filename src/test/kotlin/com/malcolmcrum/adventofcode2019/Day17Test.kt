package com.malcolmcrum.adventofcode2019

import org.junit.jupiter.api.Test

internal class Day17Test {
    @Test
    fun `simple test`() {
        val input = """
            ..#..........
            ..#..........
            #######...###
            #.#...#...#.#
            #############
            ..#...#...#..
            ..#####...^..
        """.trimIndent()

        val scaffolding = Scaffolding()
        input.forEach { scaffolding.output.invoke(it.toLong()) }
        val map = scaffolding.grid.to2dArray()
        scaffolding.getIntersections().forEach { map[it.second][it.first] = 'O' }
        draw(map)
        println(scaffolding.getIntersections().map { "$it: ${it.first * it.second}"})
    }
}