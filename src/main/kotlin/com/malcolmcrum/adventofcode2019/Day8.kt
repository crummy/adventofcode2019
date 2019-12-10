package com.malcolmcrum.adventofcode2019

import java.io.File

typealias Layer = List<Int>

const val BLACK = 0
const val WHITE = 1
const val TRANSPARENT = 2

fun decode(password: String, width: Int, height: Int): List<Layer> {
    assert(password.length % (width * height) == 0)

    return password.map { Character.getNumericValue(it) }
        .chunked(width * height)
}

fun render(layers: List<Layer>, width: Int, height: Int): Layer {
    val output = layers[0].toMutableList()
    layers.drop(1).forEach { layer ->
        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                val index = y * width + x
                if (output[index] == TRANSPARENT) output[index] = layer[index]
            }
        }
    }
    return output
}

fun Layer.bitmap(width: Int): String {
    return map {
        when (it) {
            BLACK -> "."
            WHITE -> "#"
            TRANSPARENT -> " "
            else -> "$it"
        }
    }
        .chunked(width)
        .map { it.joinToString("") }
        .joinToString("\n")
}


fun main() {
    val input = File("src/main/resources/input/day8.txt").readText()
    val width = 25
    val height = 6
    val layers = decode(input, width, height)
    val minZeroLayer = layers.minBy { layer -> layer.count { it == 0 } }!!
    val output = minZeroLayer.count { it == 1 } * minZeroLayer.count { it == 2 }

    println(output)

    val combinedLayers = render(layers, width, height)

    println(combinedLayers.bitmap(width))
}