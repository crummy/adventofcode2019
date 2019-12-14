package com.malcolmcrum.adventofcode2019

const val FUEL = "FUEL"

data class Reaction(val requirements: List<String>, val produces: List<String>) {
    companion object {
        val ORE = Reaction(emptyList(), listOf("ORE"))

        val ioRegex = "(.*) => (.*)".toRegex()
        val resourceRegex = "(\\d+) (\\w+)".toRegex()
        fun parse(string: String): Reaction {
            val (input, output) = ioRegex.matchEntire(string)!!.destructured
            val requirements = input.split(", ")
                .flatMap { resource ->
                    val (amount, name) = resourceRegex.matchEntire(resource)!!.destructured
                    name.repeat(amount.toInt())
                }
            val (amount, name) = resourceRegex.matchEntire(output)!!.destructured
            val produces = name.repeat(amount.toInt())

            return Reaction(requirements, produces)
        }
    }
}

fun String.repeat(times: Int): List<String> {
    return (0 until times).map { this }
}

fun <T> List<T>.containsOnly(value: T): Boolean {
    return this.all { it == value }
}

// Does not ignore dupes!
fun MutableList<String>.remove(other: List<String>) {
    other.forEach { this.remove(it) }
}

// Does not ignore dupes!
// adopted from https://stackoverflow.com/a/53687760/281657
fun List<String>.contains(other: List<String>): Boolean {
    var count = 0
    this.intersect(other).forEach { x -> count += listOf(this.count {it == x}, other.count {it == x}).min()!! }
    return count == other.size
}

fun List<Reaction>.produce(requirements: List<String>): List<String> {
    // Start at the end: assuming we've already produced the requirements.
    val produced = requirements.toMutableList()

    // We know we're done when produced list contains only ORE.
    while (!produced.containsOnly("ORE")) {
        val nextReaction = this.firstOrNull { produced.contains(it.produces) } ?: this.first { produced.containsAll(it.produces) }
        produced.remove(nextReaction.produces)
        produced.addAll(nextReaction.requirements)
    }

    return produced
}