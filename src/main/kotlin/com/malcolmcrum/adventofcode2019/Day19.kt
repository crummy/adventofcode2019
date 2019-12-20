package com.malcolmcrum.adventofcode2019

import java.io.File

fun getTractorBeamPoints(): Int {
    val instructions = File("src/main/resources/input/day19.txt").readText()
        .split(",").map { it.toLong() }.toLongArray()

    var beamPoints = 0
    repeat(50) { y ->
        repeat(50) { x ->
            val output = OutputCapture()
            Emulator5("tractor", instructions, input(x, y), output).run()
            beamPoints += output.value!!.toInt()
        }
    }
    return beamPoints
}

fun input(vararg args: Int): () -> Long {
    var calls = 0
    return {
        val result = args[calls].toLong()
        calls++
        result
    }
}

class OutputCapture : (Long) -> Unit {
    var value: Long? = null

    override fun invoke(output: Long) {
        value = output
    }
}

fun main() {
    val tractorPoints = getTractorBeamPoints()

    println(tractorPoints)
}