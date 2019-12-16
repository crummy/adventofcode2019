package com.malcolmcrum.adventofcode2019

import com.malcolmcrum.adventofcode2019.Droid.Entity.*
import com.malcolmcrum.adventofcode2019.Droid.Input.*
import java.io.File
import kotlin.math.abs

class Droid {
    val knownMap = mutableMapOf<Tile, Entity>()
    var oxygen: Tile? = null
    var position = Tile(0, 0)
    lateinit var lastMove: Input

    val move: () -> Long = {
        val (destination, path) = nearestUnknownTile()
        val direction = when (path.first()) {
            position + NORTH -> NORTH
            position + SOUTH -> SOUTH
            position + EAST -> EAST
            position + WEST -> WEST
            else -> error("Can't find first step from $position towards $destination in $path")
        }
        println("Found unknown tile at $destination. Moving $direction towards it, via ${path.first()}")
        lastMove = direction
        direction.code.toLong()
    }

    val update: (Long) -> Unit = {
        val output = Output.of(it.toInt())
        when (output) {
            Output.HIT_WALL -> {
                val wall = position + lastMove
                knownMap[wall] = WALL
                println("Hit wall at $wall")
            }
            Output.MOVED_STEP -> {
                position += lastMove
                knownMap[position] = EMPTY
                println("Moved to $position")
            }
            Output.MOVED_STEP_FOUND_OXYGEN -> {
                position += lastMove
                oxygen = position
                knownMap[position] = OXYGEN
                println("Found oxygen at $position")
            }
        }
    }

    // adapted from https://medium.com/@nicholas.w.swift/easy-a-star-pathfinding-7e6689c7f7b2
    private fun nearestUnknownTile(): Pair<Tile, List<Tile>> {
        val open = mutableMapOf(position to Node(position))
        val closed = mutableSetOf<Tile>()

        fun findAdjacentEmptyTile(current: Node, direction: Input): Pair<Tile, List<Tile>>? {
            val tile = current.tile + direction
            val entity = knownMap[tile]
            if (entity == null) {
                return Pair(tile, current.getPath().plus(tile).drop(1))
            } else if (entity == WALL || closed.contains(tile)) {
                // ignore it
            } else if (!open.contains(tile)) {
                open[tile] = Node(tile, current.cost + 1, current)
            } else {
                val existingCost = open[tile]!!.cost
                if (existingCost > current.cost + 1) {
                    open[tile] = Node(tile, current.cost + 1, current)
                }
            }
            return null
        }

        do {
            val current = open.minBy { (_, node) -> node.cost }!!.value
            open.remove(current.tile)
            closed.add(current.tile)
            val result = findAdjacentEmptyTile(current, NORTH)
                ?: findAdjacentEmptyTile(current, EAST)
                ?: findAdjacentEmptyTile(current, SOUTH)
                ?: findAdjacentEmptyTile(current, WEST)
            if (result != null) return result
        } while (open.isNotEmpty())
        throw MapExploredException()
    }

    fun pathFrom(start: Tile, target: Tile): List<Tile> {
        val open = mutableMapOf(start to Node(start))
        val closed = mutableSetOf<Tile>()

        fun findOxygenTile(current: Node, direction: Input): Pair<Tile, List<Tile>>? {
            val tile = current.tile + direction
            val entity = knownMap[tile] ?: error("Found an empty tile - map has not been fully explored")
            if (entity == OXYGEN) {
                return Pair(tile, current.getPath().plus(tile).drop(1))
            } else if (entity == WALL || closed.contains(tile)) {
                // ignore it
            } else if (!open.contains(tile)) {
                open[tile] = Node(tile, current.cost + 1, current)
            } else {
                val existingCost = open[tile]!!.cost
                if (existingCost > current.cost + 1) {
                    open[tile] = Node(tile, current.cost + 1, current)
                }
            }
            return null
        }

        do {
            val current = open.minBy { (_, node) -> node.costTo(target) }!!.value
            open.remove(current.tile)
            closed.add(current.tile)
            val result = findOxygenTile(current, NORTH)
                ?: findOxygenTile(current, EAST)
                ?: findOxygenTile(current, SOUTH)
                ?: findOxygenTile(current, WEST)
            if (result != null) return result.second
        } while (open.isNotEmpty())
        throw RuntimeException("Couldn't find path from $start to $target")
    }


    enum class Entity {
        WALL, EMPTY, OXYGEN
    }

    enum class Input(val code: Int, val x: Int, val y: Int) {
        NORTH(1, 0, 1),
        SOUTH(2, 0, -1),
        WEST(3, -1, 0),
        EAST(4, 1, 0)
    }

    enum class Output(val code: Int) {
        HIT_WALL(0),
        MOVED_STEP(1),
        MOVED_STEP_FOUND_OXYGEN(2);

        companion object {
            fun of(code: Int) = values().find { it.code == code } ?: error("Output not recognized: $code")
        }
    }

    data class Node(val tile: Tile, val cost: Int = 0, var parent: Node? = null) {
        fun getPath(): List<Tile> {
            val parents = mutableListOf(tile)
            var parent = this.parent
            while (parent != null) {
                parents += parent.tile
                parent = parent.parent
            }
            parents.reverse()
            return parents
        }

        fun costTo(target: Tile): Int {
            return cost + abs(tile.first - target.first) + abs(tile.second - target.second)
        }
    }

}

class MapExploredException : Exception()

operator fun Tile.plus(direction: Droid.Input): Tile {
    return Tile(first + direction.x, second + direction.y)
}

fun shortestPathToOxygen(): Int {
    val instructions = File("src/main/resources/input/day15.txt").readText()
        .split(",").map { it.toLong() }.toLongArray()
    val robot = Droid()
    try {
        Emulator5("Robot", instructions, robot.move, robot.update).run()
    } catch (e: MapExploredException) { // an awkward way to stop the computer...
        draw(robot.knownMap)
    }

    return robot.pathFrom(Tile(0, 0), robot.oxygen!!).size
}

fun draw(map: Map<Tile, Droid.Entity>) {
    var minX = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE
    var minY = Int.MAX_VALUE
    for (tile in map.keys) {
        if (tile.first < minX) minX = tile.first
        if (tile.second < minY) minY = tile.second
        if (tile.first > maxX) maxX = tile.first
        if (tile.second > maxY) maxY = tile.second
    }
    val width = abs(maxX - minX) + 1
    val height = abs(maxY - minY) + 1
    val grid = array2dOfChar(width, height, ' ')
    for ((tile, entity) in map) {
        grid[tile.second - minY][tile.first - minX] = when(entity) {
            WALL -> '#'
            EMPTY -> '.'
            OXYGEN -> '@'
        }
    }
    grid.forEach { println(it) }
}

fun main() {
    val steps = shortestPathToOxygen()
    println(steps)
}