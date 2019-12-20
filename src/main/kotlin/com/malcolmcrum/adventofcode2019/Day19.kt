package com.malcolmcrum.adventofcode2019

import java.io.File

const val BOX_SIZE = 10

val day19instructions = lazy {
    File("src/main/resources/input/day19.txt").readText()
        .split(",").map { it.toLong() }.toLongArray()
}

fun getTractorBeamPoints(): Int {
    var beamPoints = 0
    repeat(35) { y ->
        repeat(40) { x ->
            val result = checkForBeam(x, y)
            beamPoints += result
            when (result) {
                1 -> print("#")
                0 -> print("_")
            }
        }
        println()
    }
    return beamPoints
}

private fun checkForBeam(x: Int, y: Int): Int {
    return if (checkForBeam(Pair(x, y))) 1 else 0
}

private fun checkForBeam(point: Pair<Int, Int>): Boolean {
    val output = OutputCapture()
    Emulator5("tractor", day19instructions.value, input(point.first, point.second), output).run()
    return output.value!! == 1L
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

fun firstFittingBox(): Pair<Int, Int> {
    var x = 0
    (10..10000).forEach { y ->
        while (checkForBeam(x, y) == 0) x++
        println("testing box at $x, $y")
        val box = Box(x, y - 99, 99)
        if (doesBoxFit(box)) {
            return box.topLeft
        }
    }
    error("no box found")
}

fun doesBoxFit(box: Box): Boolean {
    val lowerLeft = box.bottomLeft
    val upperRight = box.topRight

    return checkForBeam(upperRight) && checkForBeam(lowerLeft)
}

class Box(x: Int, y: Int, boxSize: Int = 3) {
    val topLeft = Pair(x, y)
    val topRight = Pair(x + boxSize, y)
    val bottomLeft = Pair(x, y + boxSize)
}

fun main() {
    val tractorPoints = getTractorBeamPoints()

    println(tractorPoints)

    val (x, y) = firstFittingBox()

    println("$x, $y = ${x*10000 + y}")
}
