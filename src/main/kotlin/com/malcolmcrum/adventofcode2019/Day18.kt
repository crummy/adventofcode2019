package com.malcolmcrum.adventofcode2019

import java.io.File
import kotlin.math.abs

class Vault(
    val map: Array<CharArray>,
    val entrance: Tile,
    val doors: Map<Char, Tile>,
    val keys: Map<Char, Tile>
) {
    val distanceCache: MutableMap<KeyToKey, Int?> = mutableMapOf()

    fun collectAllKeys(keysInPocket: MutableSet<Char> = mutableSetOf(), position: Tile = entrance): Int {
        if (keysInPocket.size == keys.size) {
            println()
            return 0
        }
        val accessibleKeys = keys.filterKeys { !keysInPocket.contains(it) }
            .mapNotNull { (key, destination) ->
                distanceCache.computeIfAbsent(KeyToKey(position, destination, keysInPocket.toList().sorted())) {
                    position.canTravelTo(destination, keysInPocket)
                }?.let { key to it }
            }.toMap()
        return accessibleKeys.map { (key, distance) ->
            print("$key,")
            distance + collectAllKeys(keysInPocket.toMutableSet().apply { this.add(key) }, keys.getValue(key))
        }.min()!!
    }

    fun Tile.canTravelTo(target: Tile, keysInPocket: Set<Char>): Int? {
        val open = mutableMapOf(this to Node(this))
        val closed = mutableSetOf<Tile>()

        fun findMatchingTile(current: Node, direction: Direction): Int? {
            val tile = current.tile + direction
            val entity = map[tile]
            if (tile == target) {
                return current.getPath().size
            } else if (entity == WALL || closed.contains(tile) || entity.isLockedDoor(keysInPocket)) {
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
            val foundTile = findMatchingTile(current, Direction.UP)
                ?: findMatchingTile(current, Direction.RIGHT)
                ?: findMatchingTile(current, Direction.DOWN)
                ?: findMatchingTile(current, Direction.LEFT)
            if (foundTile != null) return foundTile
        } while (open.isNotEmpty())
        return null
    }

    private fun Char.isLockedDoor(keysInPocket: Set<Char>): Boolean {
        return doors.contains(this) && !keysInPocket.contains(this.toLowerCase())
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

    companion object {
        const val WALL = '#'
        const val ENTRANCE = '@'

        fun parse(input: String): Vault {
            val lines = input.lines()
            val height = lines.size
            val width = lines[0].length
            val array = array2dOfChar(height, width, '?')
            var entrance: Tile? = null
            val doors: MutableMap<Char, Tile> = mutableMapOf()
            val keys: MutableMap<Char, Tile> = mutableMapOf()
            lines.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    array[y][x] = char
                    when {
                        char == ENTRANCE -> entrance = Tile(x, y)
                        char.isLowerCase() -> keys[char] = Tile(x, y)
                        char.isUpperCase() -> doors[char] = Tile(x, y)
                    }
                }
            }
            return Vault(array, entrance!!, doors, keys)
        }
    }

    data class KeyToKey(
        val position: Tile,
        val destination: Tile,
        val keysInPocket: List<Char>
    )

}

private operator fun Array<CharArray>.get(tile: Tile): Char {
    return this[tile.second][tile.first]
}

private operator fun Array<CharArray>.set(position: Tile, value: Char) {
    this[position.second][position.first] = value
}

fun main() {
    val instructions = File("src/main/resources/input/day18.txt").readText()

    val vault = Vault.parse(instructions)

    println(vault.collectAllKeys())
}