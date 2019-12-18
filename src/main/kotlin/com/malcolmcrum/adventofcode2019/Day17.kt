package com.malcolmcrum.adventofcode2019

import java.io.File
import kotlin.math.abs

class Scaffolding {
    val grid = mutableMapOf<Tile, Char>()
    var row = 0
    var column = 0

    val output: (Long) -> Unit = {
        print(it.toChar())
        if (it == NEWLINE) {
            row++
            column = 0
        } else {
            grid[Tile(column, row)] = it.toChar()
            column++
        }
    }

    val input: () -> Long = {
        // scaffolding never gets input
        TODO()
    }

    fun getIntersections(): Collection<Tile> {
        return grid.filter { it.value == '#' }
            .map { it.key }
            .filter { surroundingWalls(it).size > 2 }
    }

    private fun surroundingWalls(tile: Tile): Collection<Tile> {
        return Direction.values()
            .map { direction -> tile + direction }
            .map { adjacentTile -> adjacentTile to grid[adjacentTile] }
            .filter { (_, char) -> char == '#' }
            .map { tile }
    }

    companion object {
        private const val NEWLINE = 10L
    }
}

class ScaffoldingNavigator(main: String, a: String, b: String, c: String) {
    var dustCollected = -1L
    val instructions = "$main\n$a\n$b\n$c\nn\n" // trailing n to disable output
    var instructionIndex = 0
    val input: () -> Long = {
        val result = instructions[instructionIndex].toLong()
        instructionIndex++
        result
    }
    val output: (Long) -> Unit = {
        dustCollected = it
    }
}

fun MutableMap<Tile, Char>.simulate(instructions: String) {
    var tile = this.filter { it.value == '^' }.keys.first()
    var direction = Direction.UP
    for (instruction in instructions.split(",")) {
        when {
            instruction == "R" -> direction = direction.turnRight()
            instruction == "L" -> direction = direction.turnLeft()
            instruction.toIntOrNull() != null -> repeat(instruction.toInt()) { tile += direction; this[tile] = '@' }
        }
        println("$instruction: now at $tile facing $direction")
    }
    this[tile] = '_'
}

operator fun Tile.plus(direction: Direction): Tile {
    return Tile(first + direction.x, second + direction.y)
}

fun main() {
    val instructions = File("src/main/resources/input/day17.txt").readText()
        .split(",").map { it.toLong() }.toLongArray()
    val scaffolding = Scaffolding()
    Emulator5("Scaffolding", instructions, scaffolding.input, scaffolding.output).run()
    val intersections = scaffolding.getIntersections()
    println(intersections.sumBy { it.first * it.second })

    // from https://malcolmcrum.com/aoc2019_day17solver/
    val main = "A,B,B,C,C,A,B,B,C,A"
    val a = "R,4,R,12,R,10,L,12"
    val b = "L,12,R,4,R,12"
    val c = "L,12,L,8,R,10"

    instructions[0] = 2
    val navigator = ScaffoldingNavigator(main, a, b, c)
    Emulator5("Scaffolding", instructions, navigator.input, navigator.output).run()

    println(navigator.dustCollected)
}

fun draw(map: Array<CharArray>) {
    for (row in map) {
        println()
        for (char in row) {
            print(char)
        }
    }
    println()
}


fun Map<Tile, Char>.to2dArray(): Array<CharArray> {
    var minX = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE
    var minY = Int.MAX_VALUE
    for (tile in keys) {
        if (tile.first < minX) minX = tile.first
        if (tile.second < minY) minY = tile.second
        if (tile.first > maxX) maxX = tile.first
        if (tile.second > maxY) maxY = tile.second
    }
    val width = abs(maxX - minX) + 1
    val height = abs(maxY - minY) + 1
    val grid = array2dOfChar(height, width, ' ')
    for ((tile, entity) in this) {
        grid[tile.second - minY][tile.first - minX] = entity
    }
    return grid
}