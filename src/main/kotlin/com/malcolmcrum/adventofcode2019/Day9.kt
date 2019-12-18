package com.malcolmcrum.adventofcode2019

import java.io.File

class Emulator4(
    private val name: String,
    data: LongArray,
    input: List<Long>
) {
    private val output = mutableListOf<Long>()
    private val input = input.toMutableList()
    private val data = ResizableArray(data.copyOf())
    private var pc = 0
    private var relativeBase = 0

    fun run(): List<Long> {
        do {
            val operation = tick()
        } while (operation != Operation.End)
        return output
    }

    private fun tick(): Operation {
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
            Operation.AdjustRelativeBase -> adjustRelativeBase(instruction)
            Operation.End -> end()
        }
        if (jumpAddress != null) {
            pc = jumpAddress
        } else {
            pc += operation.instructionLength
        }
        return operation
    }

    private fun end(): Int? {
        println("$name: END")
        return null
    }

    private fun adjustRelativeBase(instruction: Instruction): Int? {
        val offset = data[instruction.param1.invoke(pc + 1L)].toInt()
        println("$name: relative base = $offset")
        relativeBase += offset
        return null
    }

    private fun compareEquals(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1L)]
        val operand2 = data[instruction.param2.invoke(pc + 2L)]
        val result = if (operand1 == operand2) 1 else 0L
        val destination = instruction.param3.invoke(pc + 3L).toInt()
        println("$name: data[$destination] = $operand1 == $operand2 = $result")
        data[destination] = result
        return null
    }

    private fun compareLessThan(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1L)]
        val operand2 = data[instruction.param2.invoke(pc + 2L)]
        val result = if (operand1 < operand2) 1 else 0L
        val destination = instruction.param3.invoke(pc + 3L).toInt()
        println("$name: data[$destination] = $operand1 < $operand2 = $result")
        data[destination] = result
        return null
    }

    private fun jumpIfFalse(instruction: Instruction): Int? {
        val check = data[instruction.param1.invoke(pc + 1L)]
        return if (check == 0L) {
            data[instruction.param2.invoke(pc + 2L)].toInt()
        } else {
            null
        }
    }

    private fun jumpIfTrue(instruction: Instruction): Int? {
        val check = data[instruction.param1.invoke(pc + 1L)]
        return if (check != 0L) {
            data[instruction.param2.invoke(pc + 2L)].toInt()
        } else {
            null
        }
    }

    private fun readInput(instruction: Instruction): Int? {
        val destination = instruction.param1.invoke(pc + 1L).toInt()
        val value = input.removeAt(0)
        println("$name: data[$destination] = $value from input")
        data[destination] = value
        return null
    }

    private fun multiply(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1L)]
        val operand2 = data[instruction.param2.invoke(pc + 2L)]
        val destination = instruction.param3.invoke(pc + 3L).toInt()
        println("$name: data[$destination] = $operand1 * $operand2")
        data[destination] = operand1 * operand2
        return null
    }

    private fun add(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1L)]
        val operand2 = data[instruction.param2.invoke(pc + 2L)]
        val destination = instruction.param3.invoke(pc + 3L).toInt()
        println("$name: data[$destination] = $operand1 + $operand2")
        data[destination] = operand1 + operand2
        return null
    }

    private fun writeOutput(instruction: Instruction): Int? {
        val address = instruction.param1.invoke(pc + 1L).toInt()
        val value = data[address]
        println("$name: output += data[$address] ($value)")
        output.add(value)
        return null
    }

    private fun parseInstruction(instruction: Long): Instruction {
        val padded = instruction.toString().padStart(5, '0')
        val (A, B, C, DE) = "(\\d)(\\d)(\\d)(\\d\\d)".toRegex().matchEntire(padded)!!.destructured
        val param1 = readParameter(C.toInt())
        val param2 = readParameter(B.toInt())
        val param3 = readParameter(A.toInt())
        val operation = Operation.parse(DE)
        return Instruction(operation, param1, param2, param3)
    }

    private fun readParameter(mode: Int): (Long) -> Long {
        return when (mode) {
            IMMEDIATE_MODE -> { index -> index }
            POSITION_MODE -> { index -> data[index] }
            RELATIVE_MODE -> { index -> data[index] + relativeBase }
            else -> throw IllegalArgumentException("Illegal mode $mode")
        }
    }

    enum class Operation(val code: Int, val instructionLength: Int) {
        Add(1, 4),
        Multiply(2, 4),
        ReadInput(3, 2),
        WriteOutput(4, 2),
        JumpIfTrue(5, 3),
        JumpIfFalse(6, 3),
        LessThan(7, 4),
        Equals(8, 4),
        AdjustRelativeBase(9, 2),
        End(99, 0);

        companion object {
            private val codes: Map<Int, Operation> = values().map { it.code to it }.toMap()

            fun parse(opcode: String) =
                codes[opcode.toInt()] ?: throw IllegalArgumentException("Illegal operation $opcode")
        }
    }

    data class Instruction(
        val operation: Operation,
        val param1: (Long) -> Long,
        val param2: (Long) -> Long,
        val param3: (Long) -> Long
    )

    companion object {
        const val RELATIVE_MODE = 2
        const val IMMEDIATE_MODE = 1
        const val POSITION_MODE = 0
    }
}

class ResizableArray(private var data: LongArray) {
    operator fun get(index: Int): Long {
        return if (index >= data.size) 0 else data[index]
    }

    operator fun get(index: Long): Long {
        return if (index >= data.size) 0 else data[index.toInt()]
    }

    operator fun set(index: Int, value: Long) {
        if (index >= data.size) data = data.copyOf(index + 1)
        data[index] = value
    }

}

fun main() {
    val instructions = File("src/main/resources/input/day9.txt").readText()
        .split(",").map { it.toLong() }.toLongArray()
    val output1 = Emulator4("part1", instructions, listOf(1)).run()

    println(output1)

    val output2 = Emulator4("part2", instructions, listOf(2)).run()

    println(output2)

}