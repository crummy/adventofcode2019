package com.malcolmcrum.adventofcode2019

import java.io.File

class Emulator {
    var data: IntArray = IntArray(0)
    var pc: Int = 0

    fun load(data: IntArray) {
        this.data = data;
    }

    fun tick() {
        val opcode = data[pc]
        when (opcode) {
            99 -> return
            1 -> add(data[pc + 1], data[pc + 2], data[pc + 3])
            2 -> multiply(data[pc + 1], data[pc + 2], data[pc + 3])
            else -> throw IllegalStateException("Invalid opcode $opcode")
        }
        pc += 4
    }

    fun run() {
        pc = 0
        while (data[pc] != 99) {
            tick()
        }
    }

    private fun add(left: Int, right: Int, destination: Int) {
        data[destination] = data[left] + data[right]
    }

    private fun multiply(left: Int, right: Int, destination: Int) {
        data[destination] = data[left] * data[right]
    }

}

fun main() {
    val originalInstructions = File("src/main/resources/input/day2.txt")
        .readLines().flatMap { it.split(",") }.map { it.toInt() }.toIntArray()
    val emulator = Emulator()
    for (x in (0..99)) {
        for (y in (0..99)) {
            val instructions = originalInstructions.copyOf()
            instructions[1] = x
            instructions[2] = y
            emulator.load(instructions)
            emulator.run()
            if (emulator.data[0] == 19690720) {
                println("$x, $y")
                return
            }
        }
    }
}