package com.malcolmcrum.adventofcode2019

import java.io.File
import kotlin.math.atan2

typealias Asteroid = Pair<Int, Int>

class AsteroidBelt(val asteroids: Set<Asteroid>) {
    fun maxVisibleAsteroids(): Int {
        return asteroids.map { asteroidsVisible(it) }.max()!!
    }

    private fun asteroidsVisible(asteroid: Asteroid): Int {
        return asteroids.filter { it != asteroid }
            .map { it.angleTo(asteroid) }
            .distinct()
            .count()
    }

    private fun Asteroid.angleTo(other: Asteroid): Double {
        return atan2(first - other.first.toDouble(), second - other.second.toDouble())
    }

    companion object {
        fun parse(strings: List<String>): AsteroidBelt {
            val height = strings.size
            val width = strings[0].length
            assert(strings.all { it.length == width })

            val asteroids = mutableSetOf<Asteroid>()
            for (y in (0 until height)) {
                for (x in (0 until width)) {
                    if (strings[y][x] == '#') asteroids.add(Asteroid(x, y))
                }
            }
            return AsteroidBelt(asteroids)
        }
    }
}

fun main() {
    val input = File("src/main/resources/input/day10.txt").readLines()
    val max = AsteroidBelt.parse(input).maxVisibleAsteroids()

    println(max)
}