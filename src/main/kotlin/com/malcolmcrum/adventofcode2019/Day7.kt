package com.malcolmcrum.adventofcode2019

import java.io.File

fun maxThrusterSignal(emulator: Emulator2): Int {
    val phases = generatePhases()
    var maxThrust = 0
    for (sequence in phases) {
        val (a, b, c, d, e) = sequence
        val (bInput) = emulator.run(listOf(a, 0))
        val (cInput) = emulator.run(listOf(b, bInput))
        val (dInput) = emulator.run(listOf(c, cInput))
        val (eInput) = emulator.run(listOf(d, dInput))
        val (output) = emulator.run(listOf(e, eInput))
        if (output > maxThrust) {
            println("$a, $b, $c, $d, $e")
            maxThrust = output
        }
    }
    return maxThrust
}

fun generatePhases(): Sequence<List<Int>> {
    return sequence {
        (0..4).forEach { a ->
            (0..4).forEach { b ->
                (0..4).forEach { c ->
                    (0..4).forEach { d ->
                        (0..4).forEach { e ->
                            val phases = listOf(a, b, c, d, e)
                            if (phases.distinct().size == 5) {
                                yield(phases)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun main() {
    val input = File("src/main/resources/input/day7.txt").readText()
        .split(",").map { it.toInt() }.toIntArray()
    val emulator = Emulator2(input)
    val thrust = maxThrusterSignal(emulator)

    println(thrust)
}