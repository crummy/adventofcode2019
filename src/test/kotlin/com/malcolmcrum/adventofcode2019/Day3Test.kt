package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day3Test {
    @Test
    fun `simple test`() {
        val wire1 = parseWire("R8,U5,L5,D3")
        val wire2 = parseWire("U7,R6,D4,L4")

        val intersection = nearestIntersection(wire1, wire2)

        assertThat(intersection.manhattanDistance).isEqualTo(6)
    }

    @Test
    fun `complex test`() {
        val wire1 = parseWire("R75,D30,R83,U83,L12,D49,R71,U7,L72")
        val wire2 = parseWire("U62,R66,U55,R34,D71,R55,D58,R83")

        val intersection = nearestIntersection(wire1, wire2)

        assertThat(intersection.manhattanDistance).isEqualTo(159)
    }

    @Test
    fun `another complex test`() {
        val wire1 = parseWire("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51")
        val wire2 = parseWire("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")

        val intersection = nearestIntersection(wire1, wire2)

        assertThat(intersection.manhattanDistance).isEqualTo(135)
    }

    @Test
    fun `verify simple wire parsing`() {
        val wire = parseWire("R1")
        assertThat(wire.keys).containsOnly( Point(1, 0))
    }

    @Test
    fun `verify wire parsing with multiple steps`() {
        val wire = parseWire("R1,U1")
        assertThat(wire.keys).containsOnly(Point(1, 0), Point(1, 1))
    }

    @Test
    fun `verify wire parsing with overlap`() {
        val wire = parseWire("R1,U1,L1,D1")
        assertThat(wire.keys).containsOnly(Point(1, 0), Point(1, 1), Point(0, 1), Point(0, 0))
    }

    @Test
    fun `verify step parsing`() {
        val wire1 = parseWire("R75,D30,R83,U83,L12,D49,R71,U7,L72")
        val wire2 = parseWire("U62,R66,U55,R34,D71,R55,D58,R83")

        val steps = shortestStepsToIntersection(wire1, wire2)

        assertThat(steps).isEqualTo(610)
    }
}