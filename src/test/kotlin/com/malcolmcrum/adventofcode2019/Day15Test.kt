package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day15Test {
    @Test
    fun `test adding position to tiles`() {
        assertThat(Tile(0, 0) + Droid.Direction.NORTH).isEqualTo(Tile(0, 1))
        assertThat(Tile(4, 3) + Droid.Direction.EAST).isEqualTo(Tile(5, 3))
    }

    @Test
    fun `test node parents`() {
        val node0 = Droid.Node(Tile(0, 0))
        val node1 = Droid.Node(Tile(0, 1), 1, node0)
        val node2 = Droid.Node(Tile(0, 2), 2, node1)
        val node3 = Droid.Node(Tile(0, 3), 3, node2)

        assertThat(node0.getPath()).containsExactly(Tile(0, 0))
        assertThat(node1.getPath()).containsExactly(Tile(0, 0), Tile(0, 1))
        assertThat(node3.getPath()).containsExactly(Tile(0, 0), Tile(0, 1), Tile(0, 2), Tile(0, 3))
    }
}