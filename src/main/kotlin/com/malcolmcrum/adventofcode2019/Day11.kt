package com.malcolmcrum.adventofcode2019

import java.io.File

typealias Panel = Pair<Int, Int>

enum class Direction(private val x: Int, private val y: Int) {
    LEFT(-1, 0), UP(0, 1), RIGHT(1, 0), DOWN(0, -1);

    fun turnLeft(): Direction {
        var index = (ordinal - 1) % (values().size)
        if (index < 0) index += values().size
        return values()[index]
    }

    fun turnRight(): Direction {
        return values()[(ordinal + 1) % (values().size)]
    }

    fun move(from: Panel): Panel {
        return Panel(from.first + x, from.second + y)
    }
}

class PaintingRobot {
    private var facing = Direction.UP
    private var position = Panel(0, 0)
    private val panels: MutableMap<Panel, Long> = mutableMapOf()
    val getColour = { panels[position] ?: 0 }
    var paintedTiles = mutableSetOf<Panel>()
    var executeInstruction: (Long) -> Unit = { paint(it) }

    private val turn: (Long) -> Unit = { direction: Long ->
        facing = when (direction) {
            0L -> facing.turnLeft()
            1L -> facing.turnRight()
            else -> throw IllegalStateException("Can't understand direction $direction")
        }
        position = facing.move(position)
        println("Moved $facing to $position")
        executeInstruction = paint
    }

    private val paint: (Long) -> Unit = { colour: Long ->
        panels[position] = colour
        println("Painted $position with $colour")
        paintedTiles.add(position)
        executeInstruction = turn
    }

}

class Emulator5(
    private val name: String,
    data: LongArray,
    val input: () -> Long,
    val output: (Long) -> Unit
) {
    private val data = ResizableArray(data.copyOf())
    private var pc = 0
    private var relativeBase = 0

    fun run() {
        do {
            val operation = tick()
        } while (operation != Operation.End)
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
        val value = input.invoke()
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
        output.invoke(value)
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

fun main() {
    val instructions = File("src/main/resources/input/day11.txt").readText()
        .split(",").map { it.toLong() }.toLongArray()
    val robot = PaintingRobot()
    Emulator5("part1", instructions, robot.getColour, { robot.executeInstruction.invoke(it) }).run()

    println(robot.paintedTiles.size)
}