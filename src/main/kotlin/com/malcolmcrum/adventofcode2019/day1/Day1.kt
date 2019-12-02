package com.malcolmcrum.adventofcode2019.day1

import java.io.File
import kotlin.math.floor

fun fuelCounterUpper(mass: Int): Int {
    assert(mass >= 0)

    val third = mass.toDouble() / 3
    val roundedDown = floor(third).toInt()
    return roundedDown - 2
}

fun main() {
    val totalFuelRequired = File("src/main/resources/input/day1.txt")
        .useLines { module ->
            module.map { it.toInt() }
                .map { fuelCounterUpper(it) }
                .sum()
        }
    println(totalFuelRequired)
}