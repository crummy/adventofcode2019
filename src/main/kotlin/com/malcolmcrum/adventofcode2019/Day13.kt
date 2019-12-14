package com.malcolmcrum.adventofcode2019

import java.io.File
import java.lang.RuntimeException

typealias Tile = Pair<Int, Int>

class ArcadeGame {
    private val buffer = mutableListOf<Long>()
    val level: MutableMap<Tile, Int> = mutableMapOf()
    val bufferOutput = { output: Long ->
        buffer.add(output)
        if (buffer.size == 3) {
            val (x, y, tile) = buffer
            render(x.toInt(), y.toInt(), tile.toInt())
            buffer.clear()
        }
    }

    private fun render(x: Int, y: Int, tile: Int) {
        level[Tile(x, y)] = tile
    }
}

fun main() {
    val instructions = File("src/main/resources/input/day13.txt").readText()
        .split(",").map { it.toLong() }.toLongArray()
    val game = ArcadeGame()
    Emulator5("part1", instructions, { throw RuntimeException("No input") }, game.bufferOutput).run()

    println(game.level.count { (_, id) -> id == 2})
}
