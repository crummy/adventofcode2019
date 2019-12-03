package com.malcolmcrum.adventofcode2019

import java.io.File
import java.util.*
import kotlin.math.abs

fun nearestIntersection(a: Map<Point, Int>, b: Map<Point, Int>): Point {
    return a.keys
        .filter { it != Point.START }
        .filter { b.contains(it) }
        .minBy { it.manhattanDistance }!!
}

fun shortestStepsToIntersection(a: Map<Point, Int>, b: Map<Point, Int>): Int {
    return a
        .filter { (point, _) -> b.contains(point) }
        .map { (point, steps) -> steps + b.getValue(point) }
        .min()!!
}

fun parseWire(steps: String): Map<Point, Int> {
    val points = mutableMapOf<Point, Int>()
    var x = 0
    var y = 0
    var stepCount = 0
    for (step in steps.split(",")) {
        val instruction = step[0]
        val count = step.substring(1).toInt()
        assert(count >= 0)
        repeat(count) {
            stepCount++
            when (instruction) {
                'U' -> y++
                'D' -> y--
                'L' -> x--
                'R' -> x++
                else -> throw InputMismatchException("Invalid instruction $instruction")
            }
            points.putIfAbsent(Point(x, y), stepCount)
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

    val shortestPath = shortestStepsToIntersection(wire1, wire2)

    println(shortestPath)
}