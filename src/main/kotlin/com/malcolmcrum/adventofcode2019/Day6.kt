package com.malcolmcrum.adventofcode2019

import java.io.File

const val CENTER_OF_MASS = "COM"

class SolarSystem(val masses: Map<String, Mass>) {
    fun countOrbits(): Int {
        return masses.values.map { countOrbits(it.name) }.sum()
    }

    private fun countOrbits(mass: String): Int {
        if (mass == CENTER_OF_MASS) return 0

        val orbitee = masses[mass] ?: error("$mass orbits nothing??")
        return 1 + countOrbits(orbitee.orbits)
    }

    companion object {
        fun parse(orbits: List<String>): SolarSystem {
            val masses: MutableMap<String, Mass> = mutableMapOf()
            orbits.map { it.split(")") }
                .map { it[0] to it[1] }
                .forEach { (name, orbitedBy) ->
                    val orbiter = Mass(orbitedBy, name)
                    masses[orbitedBy] = orbiter
                }
            return SolarSystem(masses)
        }
    }
}

class Mass(val name: String, val orbits: String, val orbitedBy: MutableList<String> = mutableListOf())


fun main() {
    val input = File("src/main/resources/input/day6.txt").readLines()
    val system = SolarSystem.parse(input)

    val count = system.countOrbits()

    println(count)
}