package com.malcolmcrum.adventofcode2019

import java.io.File
import java.util.*
import kotlin.math.abs

fun nearestIntersection(a: Wire, b: Wire): Point {
    return a.points
        .filter { point -> b.intersects(point) }
        .minBy { it.manhattanDistance }!!
}

fun shortestStepsToIntersection(a: Wire, b: Wire): Int {
    return a.points
        .filter { point -> b.intersects(point) }
        .map { point -> a.stepsTo(point) + b.stepsTo(point) }
        .min()!!
}



class Wire(private val steps: Map<Point, Int>) {
    fun intersects(point: Point): Boolean {
        return steps.containsKey(point)
    }

    fun stepsTo(point: Point): Int {
        return steps.getValue(point)
    }

    val points = steps.keys

    companion object {
        fun parse(steps: String): Wire {
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
            return Wire(points)
        }
    }
}

data class Point(val x: Int, val y: Int) {
    val manhattanDistance = abs(x) + abs(y)
}

fun main() {
    val data = File("src/main/resources/input/day3.txt").readLines()
    val wire1 = Wire.parse(data[0])
    val wire2 = Wire.parse(data[1])

    val intersection = nearestIntersection(wire1, wire2)

    println(intersection.manhattanDistance)

    val shortestPath = shortestStepsToIntersection(wire1, wire2)

    println(shortestPath)
}