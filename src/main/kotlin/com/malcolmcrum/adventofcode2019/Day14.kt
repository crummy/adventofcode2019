package com.malcolmcrum.adventofcode2019

import java.io.File
import java.util.concurrent.ConcurrentHashMap

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

fun Map<String, Chemical>.synthesize(goal: String): Int {
    val totals: MutableMap<String, Int> = ConcurrentHashMap() // allows modification during iteration
    keys.forEach { totals[it] = 0 }
    totals[goal] = 1
    do {
        val chemicalsToProcess = totals.filter { it.value > 0 }.filter { it.key != ORE }.keys
        chemicalsToProcess.forEach{ name ->
            val chemical = this.getValue(name)
            totals[name] = totals.getValue(name) - chemical.amount
            chemical.produces.forEach { product ->
                totals.merge(product.chemical, product.amount, Int::plus) // totals[chemical] += amount
            }
        }
    } while (chemicalsToProcess.isNotEmpty())
    return totals[ORE]!!
}

fun main() {
    val input = File("src/main/resources/input/day14.txt").readLines()
    val reactions = parseReactions(input)

    val ore = reactions.synthesize(FUEL)

    println(ore)
}
