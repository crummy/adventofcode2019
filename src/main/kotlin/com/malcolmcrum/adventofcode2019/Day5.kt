package com.malcolmcrum.adventofcode2019

import java.io.File

class Emulator2(private val data: IntArray) {
    private var input = mutableListOf<Int>()
    private var pc: Int = 0
    private val output = mutableListOf<Int>()

    fun tick(): Operation {
        val instruction = parseInstruction(data[pc])
        val operation = instruction.operation
        when (operation) {
            Operation.Add -> add(instruction)
            Operation.Multiply -> multiply(instruction)
            Operation.ReadInput -> readInput(instruction)
            Operation.WriteOutput -> writeOutput(instruction)
            Operation.End -> {
            }
        }
        pc += operation.instructionLength
        return operation
    }

    private fun readInput(instruction: Instruction) {
        val destination = instruction.param1.invoke(pc + 1)
        val value = input.removeAt(0)
        println("data[$destination] = $value from input")
        data[destination] = value
    }

    private fun multiply(instruction: Instruction) {
        val operand1 = data[instruction.param1.invoke(pc + 1)]
        val operand2 = data[instruction.param2.invoke(pc + 2)]
        val destination = instruction.param3.invoke(pc + 3)
        println("data[$destination] = $operand1 * $operand2")
        data[destination] = operand1 * operand2
    }

    private fun add(instruction: Instruction) {
        val operand1 = data[instruction.param1.invoke(pc + 1)]
        val operand2 = data[instruction.param2.invoke(pc + 2)]
        val destination = instruction.param3.invoke(pc + 3)
        println("data[$destination] = $operand1 + $operand2")
        data[destination] = operand1 + operand2
    }

    private fun writeOutput(instruction: Instruction) {
        val address = instruction.param1.invoke(pc + 1)
        val value = data[address]
        println("output += data[$address] ($value)")
        output.add(value)
    }

    fun run(input: List<Int> = listOf()): List<Int> {
        pc = 0
        output.clear()
        this.input = input.toMutableList()
        do {
            val operation = tick()
        } while (operation != Operation.End)
        return output
    }

    fun parseInstruction(instruction: Int): Instruction {
        val padded = instruction.toString().padStart(5, '0')
        val (A, B, C, DE) = "(\\d)(\\d)(\\d)(\\d\\d)".toRegex().matchEntire(padded)!!.destructured
        println("Parsed $padded to $A, $B, $C, $DE")
        val param1 = readParameter(C.toInt())
        val param2 = readParameter(B.toInt())
        val param3 = readParameter(A.toInt())
        val operation = parseOperation(DE)
        return Instruction(operation, param1, param2, param3)
    }

    private fun readParameter(mode: Int): (Int) -> Int {
        return when (mode) {
            IMMEDIATE_MODE -> { index -> index }
            POSITION_MODE -> { index -> data[index] }
            else -> throw IllegalArgumentException("Illegal mode $mode")
        }
    }

    sealed class Operation(val code: Int, val instructionLength: Int) {
        object Add : Operation(1, 4)
        object Multiply : Operation(2, 4)
        object ReadInput : Operation(3, 2)
        object WriteOutput : Operation(4, 2)
        object End : Operation(99, 0)
    }

    fun parseOperation(opcode: String): Operation {
        return when (opcode.toInt()) {
            Operation.Add.code -> Operation.Add
            Operation.Multiply.code -> Operation.Multiply
            Operation.ReadInput.code -> Operation.ReadInput
            Operation.WriteOutput.code -> Operation.WriteOutput
            Operation.End.code -> Operation.End
            else -> throw IllegalArgumentException("Illegal operation $opcode")
        }
    }

    data class Instruction(
        val operation: Operation,
        val param1: (Int) -> Int,
        val param2: (Int) -> Int,
        val param3: (Int) -> Int
    )

    companion object {
        const val IMMEDIATE_MODE = 1
        const val POSITION_MODE = 0
    }
}

fun main() {
    val originalInstructions = File("src/main/resources/input/day5.txt")
        .readLines().flatMap { it.split(",") }.map { it.toInt() }.toIntArray()
    val emulator = Emulator2(originalInstructions)
    val output = emulator.run(listOf(1))

    println(output)
}
