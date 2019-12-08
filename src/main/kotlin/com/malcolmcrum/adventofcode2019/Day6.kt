package com.malcolmcrum.adventofcode2019

import java.io.File

const val CENTER_OF_MASS = "COM"

fun parseOrbits(string: String): Map<String, String> {
    return string.trim().lines().map { it.split(")") }
        .map { it[1] to it[0] }
        .toMap()
}

fun countOrbits(orbits: Map<String, String>): Int {
    return orbits.values.map { countOrbits(orbits, it) }.sum()
}

fun countOrbits(orbits: Map<String, String>, mass: String): Int {
    if (mass == CENTER_OF_MASS) return 1

    val orbitsThisMass = orbits[mass] ?: error("$mass orbits nothing??")
    return 1 + countOrbits(orbits, orbitsThisMass)
}

fun main() {
    val input = File("src/main/resources/input/day6.txt").readText()
    val orbits = parseOrbits(input)

    val count = countOrbits(orbits)

    println(count)
}