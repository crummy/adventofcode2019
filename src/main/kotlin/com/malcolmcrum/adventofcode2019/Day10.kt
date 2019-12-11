package com.malcolmcrum.adventofcode2019

import java.io.File
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow

typealias Asteroid = Pair<Int, Int>

class AsteroidBelt(val asteroids: Set<Asteroid>) {
    fun maxVisibleAsteroids(): Pair<Asteroid, Int> {
        return asteroids.map { it to asteroidsVisible(it) }
            .maxBy { it.second }!!
    }

    private fun asteroidsVisible(asteroid: Asteroid): Int {
        return asteroids.filter { it != asteroid }
            .map { it.angleTo(asteroid) }
            .distinct()
            .count()
    }

    fun vaporizeAsteroids(asteroid: Asteroid): List<Asteroid> {
        val asteroidAngles = asteroids.filter { it != asteroid }
            .groupBy { it.angleTo(asteroid) }
            .map { (angle, asteroids) -> angle to asteroids.sortedBy { it.distanceSquared(asteroid) }.toMutableList() }
            .toMap()

        val vaporizedAsteroids = mutableListOf<Asteroid>()
        while (vaporizedAsteroids.size < asteroids.size - 1) { // we don't destroy the one we're sitting on
            for (angle in asteroidAngles.keys.sorted()) {
                val asteroidsAtAngle = asteroidAngles.getValue(angle)
                if (asteroidsAtAngle.isNotEmpty()) {
                    val vaporized = asteroidsAtAngle.removeAt(0)
                    println("Vaporized #${vaporizedAsteroids.size}: $vaporized at angle $angle")
                    vaporizedAsteroids.add(vaporized)
                }
            }
            println("Finished sweep")
        }
        return vaporizedAsteroids
    }

    private fun Asteroid.angleTo(other: Asteroid): Double {
        val radians = atan2(other.second - second.toDouble(), other.first - first.toDouble()) + 3*PI/2
        return Math.toDegrees(radians) % 360
    }

    private fun Asteroid.distanceSquared(other: Asteroid): Double {
        return (first - other.first.toDouble()).pow(2) + (second - other.second.toDouble()).pow(2)
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
    val asteroidBelt = AsteroidBelt.parse(input)
    val (asteroid, visible) = asteroidBelt.maxVisibleAsteroids()

    println(visible)

    val vaporizedAsteroids = asteroidBelt.vaporizeAsteroids(asteroid)

    println(vaporizedAsteroids[199].first * 100 + vaporizedAsteroids[199].second)
}