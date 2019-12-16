package com.malcolmcrum.adventofcode2019

import java.io.File
import kotlin.math.abs

const val FUEL = "FUEL"
const val ORE = "ORE"

data class Produces(val chemical: String, val amount: Int)
data class Chemical(val name: String, val amount: Int, val produces: List<Produces>) {
    companion object {
        private val ioRegex = "(.*) => (.*)".toRegex()
        private val resourceRegex = "(\\d+) (\\w+)".toRegex()
        fun parse(string: String): Chemical {
            val (input, output) = ioRegex.matchEntire(string)!!.destructured
            val produces = input.split(", ")
                .map { resource ->
                    val (amount, name) = resourceRegex.matchEntire(resource)!!.destructured
                    Produces(name, amount.toInt())
                }
            val (amount, name) = resourceRegex.matchEntire(output)!!.destructured
            return Chemical(name, amount.toInt(), produces)
        }
    }
}

fun parseReactions(lines: List<String>): Map<String, Chemical> {
    return lines.map {
        val chemical = Chemical.parse(it)
        chemical.name to chemical
    }.toMap()
}

fun Map<String, Chemical>.synthesizeFuel(amount: Long = 1L): Long {
    val totals: MutableMap<String, Long> = entries.map { it.key to 0L }
        .toMap().toMutableMap()
        .apply { this[FUEL] = amount }
    do {
        // First, calculate chemical in bulk.
        var chemicalsToProcess = totals.filter { it.value > 0 }.filter { it.key != ORE }.keys
        chemicalsToProcess.forEach { name ->
            val chemical = this.getValue(name)
            val reactionIterations = totals.getValue(name) / chemical.amount // produce chemical in bulk
            totals[name] = totals.getValue(name) % chemical.amount // some is left over
            chemical.produces.forEach { product ->
                totals.merge(product.chemical, product.amount * reactionIterations, Long::plus) // totals[chemical] += amount
            }
        }
        // Then do the remainders.
        chemicalsToProcess = totals.filter { it.value > 0 }.filter { it.key != ORE }.keys
        chemicalsToProcess.forEach { name ->
            val chemical = this.getValue(name)
            totals[name] = totals.getValue(name) - chemical.amount
            chemical.produces.forEach { product ->
                totals.merge(product.chemical, product.amount.toLong(), Long::plus) // totals[chemical] += amount
            }
        }
    } while (chemicalsToProcess.isNotEmpty())
    return totals[ORE]!!
}

fun Map<String, Chemical>.fuelGivenOre(amount: Long): Long {
    val fuelForOneOre = synthesizeFuel(1)
    var min = 0L
    var max = amount
    var fuelGuess: Long
    do {
        fuelGuess = (max - min) / 2 + min
        val ore = synthesizeFuel(fuelGuess)
        println("$fuelGuess fuel requires $ore ore, min: $min, max: $max")
        if (ore < amount) min = fuelGuess + 1
        else if (ore > amount) max = fuelGuess - 1
    } while (abs(ore - amount) > fuelForOneOre)
    return fuelGuess
}

fun main() {
    val input = File("src/main/resources/input/day14.txt").readLines()
    val reactions = parseReactions(input)

    val ore = reactions.synthesizeFuel()

    println(ore)

    val fuelForOneTrillionOre = reactions.fuelGivenOre(1000000000000)

    println(fuelForOneTrillionOre)
}
