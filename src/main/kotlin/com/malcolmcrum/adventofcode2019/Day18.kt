package com.malcolmcrum.adventofcode2019

import java.io.File
import kotlin.math.abs

class Vault(
    private val map: Array<CharArray>,
    private val doors: Map<Char, Tile>,
    private val keys: Map<Char, Tile>
) {
    private val keyPaths: Map<KeyPair, KeyPath>

    init {
        val keyPaths: MutableMap<KeyPair, KeyPath> = mutableMapOf()

        keys.forEach { (key, location) ->
            keys.filter { it.key != key }.forEach { (otherKey, otherLocation) ->
                val keyPair = KeyPair.create(key, otherKey)
                keyPaths.computeIfAbsent(keyPair) {
                    val path = location.pathTo(otherLocation)
                    val doorsBetween = doors.filter { (_, door) -> path.contains(door) }
                        .map { it.key }
                        .toList()
                    KeyPath(path.size, doorsBetween)
                }
            }
        }

        this.keyPaths = keyPaths
    }

    fun collectAllKeys(keysInPocket: Set<Char> = setOf('@'), lastKey: Char = '@'): Int {
        if (keysInPocket.size == keys.size) {
            return 0
        }
        return keys.keys
            .filter { !keysInPocket.contains(it) }
            .filter { it != lastKey }
            .mapNotNull { key ->
                val keyPair = KeyPair.create(lastKey, key)
                val (distance, doorsInBetween) = keyPaths.getValue(keyPair)
                val canReachKey = doorsInBetween.all { keysInPocket.contains(it.toLowerCase()) }
                return@mapNotNull if (canReachKey) {
                    val total = distance + collectAllKeys(keysInPocket + key, key)
                    if (keysInPocket.isEmpty()) {
                        println("$keysInPocket: $total")
                    }
                    total
                } else null
            }.min()!!
    }

    fun Tile.pathTo(target: Tile): List<Tile> {
        val open = mutableMapOf(this to Node(this))
        val explored = mutableSetOf<Tile>()

        fun findMatchingTile(current: Node, direction: Direction): List<Tile>? {
            val tile = current.tile + direction
            val entity = map[tile]
            if (tile == target) {
                return current.getPath().plus(tile).drop(1)
            } else if (entity == WALL || explored.contains(tile)) {
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
            explored.add(current.tile)
            val foundTile = findMatchingTile(current, Direction.UP)
                ?: findMatchingTile(current, Direction.RIGHT)
                ?: findMatchingTile(current, Direction.DOWN)
                ?: findMatchingTile(current, Direction.LEFT)
            if (foundTile != null) return foundTile
        } while (open.isNotEmpty())
        throw Exception("No path found from $this to $target")
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

    data class KeyPath(val distance: Int, val doorsBetween: List<Char>)

    data class KeyPair(val key1: Char, val key2: Char) {
        companion object {
            fun create(key1: Char, key2: Char): KeyPair {
                return if (key1 < key2) KeyPair(key1, key2) else KeyPair(key2, key1)
            }
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
            val doors: MutableMap<Char, Tile> = mutableMapOf()
            val keys: MutableMap<Char, Tile> = mutableMapOf()
            lines.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    array[y][x] = char
                    when {
                        char == ENTRANCE -> keys[char] = Tile(x, y)
                        char.isLowerCase() -> keys[char] = Tile(x, y)
                        char.isUpperCase() -> doors[char] = Tile(x, y)
                    }
                }
            }
            return Vault(array, doors, keys)
        }
    }
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