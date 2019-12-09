package com.malcolmcrum.adventofcode2019

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.io.File

class Emulator3(
    private val name: String,
    data: IntArray,
    private val input: Channel<Int>,
    private val output: Channel<Int>
) {
    private val data = data.copyOf()
    private var pc: Int = 0

    suspend fun tick(): Operation {
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

    private fun compareEquals(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1)]
        val operand2 = data[instruction.param2.invoke(pc + 2)]
        val result = if (operand1 == operand2) 1 else 0
        val destination = instruction.param3.invoke(pc + 3)
        println("$name: data[$destination] = $operand1 == $operand2 = $result")
        data[destination] = result
        return null
    }

    private fun compareLessThan(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1)]
        val operand2 = data[instruction.param2.invoke(pc + 2)]
        val result = if (operand1 < operand2) 1 else 0
        val destination = instruction.param3.invoke(pc + 3)
        println("$name: data[$destination] = $operand1 < $operand2 = $result")
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

    private suspend fun readInput(instruction: Instruction): Int? {
        val destination = instruction.param1.invoke(pc + 1)
        println("$name: waiting on input...")
        val value = withContext(Dispatchers.Default) { input.receive() }
        println("$name: data[$destination] = $value from input")
        data[destination] = value
        return null
    }

    private fun multiply(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1)]
        val operand2 = data[instruction.param2.invoke(pc + 2)]
        val destination = instruction.param3.invoke(pc + 3)
        println("$name: data[$destination] = $operand1 * $operand2")
        data[destination] = operand1 * operand2
        return null
    }

    private fun add(instruction: Instruction): Int? {
        val operand1 = data[instruction.param1.invoke(pc + 1)]
        val operand2 = data[instruction.param2.invoke(pc + 2)]
        val destination = instruction.param3.invoke(pc + 3)
        println("$name: data[$destination] = $operand1 + $operand2")
        data[destination] = operand1 + operand2
        return null
    }

    private fun writeOutput(instruction: Instruction): Int? {
        val address = instruction.param1.invoke(pc + 1)
        val value = data[address]
        println("$name: output += data[$address] ($value)")
        GlobalScope.launch { output.send(value) }
        return null
    }

    suspend fun run() {
        do {
            val operation = tick()
        } while (operation != Operation.End)
    }

    fun parseInstruction(instruction: Int): Instruction {
        val padded = instruction.toString().padStart(5, '0')
        val (A, B, C, DE) = "(\\d)(\\d)(\\d)(\\d\\d)".toRegex().matchEntire(padded)!!.destructured
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

fun maxThrusterSignal(emulator: Emulator2): Int {
    val phases = generatePhases()
    var maxThrust = 0
    for (sequence in phases) {
        val (a, b, c, d, e) = sequence
        val (bInput) = emulator.run(listOf(a, 0))
        val (cInput) = emulator.run(listOf(b, bInput))
        val (dInput) = emulator.run(listOf(c, cInput))
        val (eInput) = emulator.run(listOf(d, dInput))
        val (output) = emulator.run(listOf(e, eInput))
        if (output > maxThrust) maxThrust = output
    }
    return maxThrust
}

fun maxThrusterSignalFeedback(instructions: IntArray): Int {
    val phases = generatePhases((5..9))
    var maxThrust = 0
    for (sequence in phases) {
        val (a, b, c, d, e) = sequence
        val aInput = SpyChannel()
        val bInput = Channel<Int>(Channel.UNLIMITED)
        val cInput = Channel<Int>(Channel.UNLIMITED)
        val dInput = Channel<Int>(Channel.UNLIMITED)
        val eInput = Channel<Int>(Channel.UNLIMITED)
        runBlocking {
            launch { Emulator3("A", instructions, aInput, bInput).run() }
            launch { Emulator3("B", instructions, bInput, cInput).run() }
            launch { Emulator3("C", instructions, cInput, dInput).run() }
            launch { Emulator3("D", instructions, dInput, eInput).run() }
            launch { Emulator3("E", instructions, eInput, aInput).run() }
            aInput.send(a);
            aInput.send(0)
            bInput.send(b)
            cInput.send(c)
            dInput.send(d)
            eInput.send(e)
        }
        if (aInput.lastInput!! > maxThrust) {
            println("$a, $b, $c, $d, $e: ${aInput.lastInput}")
            maxThrust = aInput.lastInput!!
        }
    }
    return maxThrust
}

class SpyChannel(private val delegate: Channel<Int> = Channel(Channel.UNLIMITED)) : Channel<Int> by delegate {
    var lastInput: Int? = null

    override suspend fun send(element: Int) {
        lastInput = element
        delegate.send(element)
    }
}

fun generatePhases(range: IntRange = (0..4)): Sequence<List<Int>> {
    return sequence {
        range.forEach { a ->
            range.forEach { b ->
                range.forEach { c ->
                    range.forEach { d ->
                        range.forEach { e ->
                            val phases = listOf(a, b, c, d, e)
                            if (phases.distinct().size == 5) {
                                yield(phases)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun main() {
    val input = File("src/main/resources/input/day7.txt").readText()
        .split(",").map { it.toInt() }.toIntArray()
    val thrust = maxThrusterSignalFeedback(input)

    println(thrust)
}