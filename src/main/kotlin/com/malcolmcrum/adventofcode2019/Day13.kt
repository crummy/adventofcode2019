package com.malcolmcrum.adventofcode2019

import java.io.File

typealias Tile = Pair<Int, Int>

class ArcadeGame {
    private val buffer = mutableListOf<Long>()
    var score = 0L
    lateinit var ball: Tile
    lateinit var paddle: Tile
    val level: MutableMap<Tile, Int> = mutableMapOf()
    val bufferOutput = { output: Long ->
        buffer.add(output)
        if (buffer.size == 3) {
            val (x, y, parameter) = buffer
            if (x == -1L && y == 0L) {
                score = parameter
            } else {
                render(x.toInt(), y.toInt(), parameter.toInt())
            }
            buffer.clear()
        }
    }

    private fun render(x: Int, y: Int, tileId: Int) {
        val tile = Tile(x, y)
        level[tile] = tileId
        if (tileId == 4) ball = tile
        else if (tileId == 3) paddle = tile
    }
}

fun getJoystickInput(game: ArcadeGame): Long {
    return when {
        game.paddle.first < game.ball.first -> 1
        game.paddle.first > game.ball.first -> -1
        else -> 0
    }
}

fun main() {
    val instructions = File("src/main/resources/input/day13.txt").readText()
        .split(",").map { it.toLong() }.toLongArray()
    val game = ArcadeGame()
    val emulator = Emulator5("part1", instructions, { getJoystickInput(game) }, game.bufferOutput)
    emulator.set(0, 2L) // override requirement for quarters
    emulator.run()

    println(game.score)
}
