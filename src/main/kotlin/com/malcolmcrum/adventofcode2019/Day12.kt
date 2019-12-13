package com.malcolmcrum.adventofcode2019

import java.io.File
import kotlin.math.abs

fun Collection<Moon>.simulateMotion() {
    for (moon in this) {
        for (other in this.filter { it != moon }) {
            if (moon.pos.x < other.pos.x) moon.vel.x++
            else if (moon.pos.x > other.pos.x) moon.vel.x--
            if (moon.pos.y < other.pos.y) moon.vel.y++
            else if (moon.pos.y > other.pos.y) moon.vel.y--
            if (moon.pos.z < other.pos.z) moon.vel.z++
            else if (moon.pos.z > other.pos.z) moon.vel.z--
        }
    }
    for (moon in this) {
        moon.pos += moon.vel
    }
}

fun Collection<Moon>.totalEnergy(): Int {
    return this.sumBy { it.potentialEnergy() * it.kineticEnergy() }
}

data class Moon(val pos: Vector3, val vel: Vector3 = Vector3(0, 0, 0)) {
    fun potentialEnergy() = with(pos) { abs(x) + abs(y) + abs(z) }

    fun kineticEnergy() = with(vel) { abs(x) + abs(y) + abs(z) }

    constructor(x: Int, y: Int, z: Int) : this(Vector3(x, y, z))
}

data class Vector3(var x: Int, var y: Int, var z: Int) {
    operator fun plusAssign(other: Vector3) {
        x += other.x
        y += other.y
        z += other.z
    }
}

fun main() {
    val regex = "<x=([-]?\\d+), y=([-]?\\d+), z=([-]?\\d+)>".toRegex()
    val moons = File("src/main/resources/input/day12.txt").readLines()
        .map { line ->
            val (x, y, z) = regex.matchEntire(line)!!.destructured
            Moon(x.toInt(), y.toInt(), z.toInt())
        }

    repeat(1000) { moons.simulateMotion() }

    println(moons.totalEnergy())
}