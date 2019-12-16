package com.malcolmcrum.adventofcode2019

import java.io.File
import kotlin.math.abs

fun fft(digits: String, phases: Int = 1): String {
    var input = digits.map { Character.getNumericValue(it) }
    repeat(phases) {
        input = fft(input)
    }
    return input.joinToString(separator = "") { it.toString() }
}

fun fft(digits: List<Int>): List<Int> {
    return digits.indices.map { outputDigit ->
        val pattern = pattern(digits.size, outputDigit)
        digits.mapIndexed { index, digit -> digit * pattern[index] }
            .sum()
    }.map { abs(it % 10) }

}

fun pattern(length: Int, iteration: Int): List<Int> {
    val pattern = mutableListOf<Int>()
    do {
        repeat(iteration + 1) { pattern += 0 }
        repeat(iteration + 1) { pattern += 1 }
        repeat(iteration + 1) { pattern += 0 }
        repeat(iteration + 1) { pattern += -1 }
    } while (pattern.size < length + 1) // +1 to offset the skip 1 on the next line
    return pattern.subList(1, length + 1)
}

fun main() {
    val instructions = File("src/main/resources/input/day16.txt").readText()

    val output = fft(instructions, 100)

    println(output.substring(0, 8))
}
