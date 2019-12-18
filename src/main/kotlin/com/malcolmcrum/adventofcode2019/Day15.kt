package com.malcolmcrum.adventofcode2019

import com.malcolmcrum.adventofcode2019.Droid.Entity.*
import com.malcolmcrum.adventofcode2019.Droid.Direction.*
import java.io.File
import kotlin.math.abs

class Droid {
    val knownMap = mutableMapOf<Tile, Entity>()
    var oxygen: Tile? = null
    var position = Tile(0, 0)
    lateinit var lastMove: Direction

    val move: () -> Long = {
        val (destination, path) = nearestUnknownTile() ?: throw MapExploredException()
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

    private fun nearestUnknownTile(): Pair<Tile, List<Tile>>? {
        return findTile(position, position) { _, entity -> entity == null }
    }

    fun pathFrom(start: Tile, target: Tile): List<Tile> {
        return findTile(start, target) { tile, _ -> tile == oxygen}!!.second
    }

    // haha oh jeez. so wasteful
    fun maxDistanceFrom(start: Tile): Int {
        return knownMap.filter { it.value == EMPTY }
            .map { findTile(start, it.key) { tile, _ -> tile == it.key }!! }
            .map { it.second.size }
            .max()!!
    }

    // adapted from https://medium.com/@nicholas.w.swift/easy-a-star-pathfinding-7e6689c7f7b2
    fun findTile(start: Tile, target: Tile, test: (Tile, Entity?) -> Boolean): Pair<Tile, List<Tile>>? {
        val open = mutableMapOf(start to Node(start))
        val closed = mutableSetOf<Tile>()

        fun findMatchingTile(current: Node, direction: Direction): Pair<Tile, List<Tile>>? {
            val tile = current.tile + direction
            val entity = knownMap[tile]
            if (test.invoke(tile, entity)) {
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
            val result = findMatchingTile(current, NORTH)
                ?: findMatchingTile(current, EAST)
                ?: findMatchingTile(current, SOUTH)
                ?: findMatchingTile(current, WEST)
            if (result != null) return result
        } while (open.isNotEmpty())
        return null
    }




    enum class Entity {
        WALL, EMPTY, OXYGEN
    }

    enum class Direction(val code: Int, val x: Int, val y: Int) {
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

operator fun Tile.plus(direction: Droid.Direction): Tile {
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
    val instructions = File("src/main/resources/input/day15.txt").readText()
        .split(",").map { it.toLong() }.toLongArray()
    val robot = Droid()
    try {
        Emulator5("Robot", instructions, robot.move, robot.update).run()
    } catch (e: MapExploredException) { // an awkward way to stop the computer...
        draw(robot.knownMap)
    }

    val steps = robot.pathFrom(Tile(0, 0), robot.oxygen!!).size
    println(steps)

    val minutesUntilFullOxygen = robot.maxDistanceFrom(robot.oxygen!!)

    println(minutesUntilFullOxygen)

}