package com.malcolmcrum.adventofcode2019

import java.io.File

class Emulator2(private val data: IntArray) {
    private val originalData = data.copyOf()
    private var input = mutableListOf<Int>()
    private var pc: Int = 0
    private val output = mutableListOf<Int>()

    fun tick(): Operation {
        val instruction = parseInstruction(data[pc])
        val operation = instruction.operation
        val jumpAddress = when (operation) {
            Operation.Add -> add(instruction)
            Operation.Multiply -> multiply(instruction)
            Operation.ReadInput -> readInput(instruction)
            Operation.WriteOutput -> writeOutput(instruction)
            Operation.JumpIfTrue -> jumpIfTrue(instruction)
            Operation.JumpIfFalse -> jumpIfFalse(instruction)
            Operation.LessThan -> compareLessThan(instruction)
            Operation.Equals -> compareEquals(instruction)
            Operation.End -> null
        }
        if (jumpAddress != null) {
            pc = jumpAddress
        } else {
            pc += operation.instructionLength
        }
        return operation
    }

    private fun compareEquals(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1)]
        val operand2 = data[instruction.param2.invoke(pc + 2)]
        val result = if (operand1 == operand2) 1 else 0
        val destination = instruction.param3.invoke(pc + 3)
        println("data[$destination] = $operand1 == $operand2 = $result")
        data[destination] = result
        return null
    }

    private fun compareLessThan(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1)]
        val operand2 = data[instruction.param2.invoke(pc + 2)]
        val result = if (operand1 < operand2) 1 else 0
        val destination = instruction.param3.invoke(pc + 3)
        println("data[$destination] = $operand1 < $operand2 = $result")
        data[destination] = result
        return null
    }

    private fun jumpIfFalse(instruction: Instruction): Int? {
        val check = data[instruction.param1.invoke(pc + 1)]
        return if (check == 0) {
            data[instruction.param2.invoke(pc + 2)]
        } else {
            null
        }
    }

    private fun jumpIfTrue(instruction: Instruction): Int? {
        val check = data[instruction.param1.invoke(pc + 1)]
        return if (check != 0) {
            data[instruction.param2.invoke(pc + 2)]
        } else {
            null
        }
    }

    private fun readInput(instruction: Instruction): Int? {
        val destination = instruction.param1.invoke(pc + 1)
        val value = input.removeAt(0)
        println("data[$destination] = $value from input")
        data[destination] = value
        return null
    }

    private fun multiply(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1)]
        val operand2 = data[instruction.param2.invoke(pc + 2)]
        val destination = instruction.param3.invoke(pc + 3)
        println("data[$destination] = $operand1 * $operand2")
        data[destination] = operand1 * operand2
        return null
    }

    private fun add(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1)]
        val operand2 = data[instruction.param2.invoke(pc + 2)]
        val destination = instruction.param3.invoke(pc + 3)
        println("data[$destination] = $operand1 + $operand2")
        data[destination] = operand1 + operand2
        return null
    }

    private fun writeOutput(instruction: Instruction): Int? {
        val address = instruction.param1.invoke(pc + 1)
        val value = data[address]
        println("output += data[$address] ($value)")
        output.add(value)
        return null
    }

    fun run(input: List<Int> = listOf()): List<Int> {
        pc = 0
        output.clear()
        originalData.copyInto(data)
        this.input = input.toMutableList()
        do {
            val operation = tick()
        } while (operation != Operation.End)
        return output
    }

    fun parseInstruction(instruction: Int): Instruction {
        val padded = instruction.toString().padStart(5, '0')
        val (A, B, C, DE) = "(\\d)(\\d)(\\d)(\\d\\d)".toRegex().matchEntire(padded)!!.destructured
        print("Parsed $padded to $A, $B, $C, $DE: ")
        val param1 = readParameter(C.toInt())
        val param2 = readParameter(B.toInt())
        val param3 = readParameter(A.toInt())
        val operation = parseOperation(DE)
        println(operation)
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
        object JumpIfTrue : Operation(5, 3)
        object JumpIfFalse : Operation(6, 3)
        object LessThan : Operation(7, 4)
        object Equals : Operation(8, 4)
        object End : Operation(99, 0)
    }

    fun parseOperation(opcode: String): Operation {
        return when (opcode.toInt()) {
            Operation.Add.code -> Operation.Add
            Operation.Multiply.code -> Operation.Multiply
            Operation.ReadInput.code -> Operation.ReadInput
            Operation.WriteOutput.code -> Operation.WriteOutput
            Operation.JumpIfTrue.code -> Operation.JumpIfTrue
            Operation.JumpIfFalse.code -> Operation.JumpIfFalse
            Operation.LessThan.code -> Operation.LessThan
            Operation.Equals.code -> Operation.Equals
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
    val instructions = File("src/main/resources/input/day5.txt")
        .readLines().flatMap { it.split(",") }.map { it.toInt() }.toIntArray()
    val emulator = Emulator2(instructions)
    val output = emulator.run(listOf(1))

    println(output)

    val output2 = emulator.run(listOf(5))

    println(output2)
}
