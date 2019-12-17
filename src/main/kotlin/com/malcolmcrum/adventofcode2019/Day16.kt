package com.malcolmcrum.adventofcode2019

import java.io.File
import kotlin.math.abs

// 1. The last digit never changes.
// 2. The value of any digit is dependent only on following digits
// Thus, we can solve by just working backwards to our offset point
fun findMessage(digits: String): String {
    val offset = digits.substring(0, 7).toInt()
    val fullDigits = digits.repeat(10000)
    val buffer = fullDigits.map { Character.getNumericValue(it) }.toMutableList()
    repeat(100) {
        repeat(fullDigits.length - offset) { digit ->
            val index = buffer.size - digit - 2 // Because last digit never changes
            buffer[index] += buffer[index + 1]
            buffer[index] %= 10
        }
    }
    return buffer.subList(offset, offset + 8).joinToString(separator = "")
}

fun fft(digits: String, phases: Int = 1, range: IntRange = digits.indices): String {
    var input = digits.map { Character.getNumericValue(it) }
        .subList(range.first, range.last + 1)
    repeat(phases) {
        input = fft(input, range.first)
    }
    return input.joinToString(separator = "") { it.toString() }
}

private fun fft(digits: List<Int>, initialIteration: Int): List<Int> {
    return digits.indices.map { iteration ->
        digits.mapIndexed { index, digit -> digit * multiplier(index, initialIteration + iteration) }
            .sum()
    }.map { abs(it % 10) }
}

fun multiplier(digit: Int, iteration: Int): Int {
    val result = ((digit + 1) / (iteration + 1)) % (4)
    return when (result) {
        0 -> 0
        1 -> 1
        2 -> 0
        3 -> -1
        else -> error("odd result for $digit, $iteration")
    }
}

fun main() {
    val instructions = File("src/main/resources/input/day16.txt").readText()

    val output = fft(instructions, 100)

    println(output.substring(0, 8))

    println(findMessage(instructions))
}
