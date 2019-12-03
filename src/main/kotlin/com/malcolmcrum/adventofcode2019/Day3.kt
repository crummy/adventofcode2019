package com.malcolmcrum.adventofcode2019

import java.io.File
import java.util.*
import kotlin.math.abs

fun nearestIntersection(a: Set<Point>, b: Set<Point>): Point {
    return a
        .filter { it != Point.START }
        .filter { b.contains(it) }
        .minBy { it.manhattanDistance }!!
}

fun parseWire(steps: String): Set<Point> {
    val points = mutableSetOf<Point>()
    var x = 0
    var y = 0
    points.add(Point(x, y))
    for (step in steps.split(",")) {
        val instruction = step[0]
        val count = step.substring(1).toInt()
        assert(count >= 0)
        repeat(count) {
            when (instruction) {
                'U' -> y++
                'D' -> y--
                'L' -> x--
                'R' -> x++
                else -> throw InputMismatchException("Invalid instruction $instruction")
            }
            points.add(Point(x, y))
        }
    }
    return points
}

data class Point(val x: Int, val y: Int) {
    val manhattanDistance = abs(x) + abs(y)

    companion object {
        val START = Point(0, 0)
    }
}

fun main() {
    val data = File("src/main/resources/input/day3.txt").readLines()
    val wire1 = parseWire(data[0])
    val wire2 = parseWire(data[1])

    val intersection = nearestIntersection(wire1, wire2)

    println(intersection.manhattanDistance)
}