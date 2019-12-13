package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day11Test {
    @Test
    fun `test robot without computer`() {
        val robot = PaintingRobot()
        robot.executeInstruction(1) // paint white at (0, 0)
        robot.executeInstruction(0) // turn left, move forward to (-1, 0)
        robot.executeInstruction(0) // paint black at (-1, 0)
        robot.executeInstruction(0) // turn left, move forward to (-1, -1)
        robot.executeInstruction(1) // paint black
        robot.executeInstruction(0) // turn left
        robot.executeInstruction(1) // paint black
        robot.executeInstruction(0) // turn left
        robot.executeInstruction(0) // paint black
        robot.executeInstruction(1) // turn right
        robot.executeInstruction(1) // paint black
        robot.executeInstruction(0) // turn left
        robot.executeInstruction(1) // paint black
        robot.executeInstruction(0) // turn left

        assertThat(robot.panels).hasSize(6)
    }

    @Test
    fun `test turning left`() {
        assertThat(Direction.UP.turnLeft()).isEqualTo(Direction.LEFT)
        assertThat(Direction.LEFT.turnLeft()).isEqualTo(Direction.DOWN)
        assertThat(Direction.DOWN.turnLeft()).isEqualTo(Direction.RIGHT)
        assertThat(Direction.RIGHT.turnLeft()).isEqualTo(Direction.UP)
    }

    @Test
    fun `test turning right`() {
        assertThat(Direction.UP.turnRight()).isEqualTo(Direction.RIGHT)
        assertThat(Direction.RIGHT.turnRight()).isEqualTo(Direction.DOWN)
        assertThat(Direction.DOWN.turnRight()).isEqualTo(Direction.LEFT)
        assertThat(Direction.LEFT.turnRight()).isEqualTo(Direction.UP)
    }
}