package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

internal class Day14Test {
    @Test
    fun `test parsing`() {
        assertThat(Reaction.parse("2 ORE => 2 A")).isEqualTo(Reaction(listOf("ORE", "ORE"), listOf("A", "A")))
        assertThat(Reaction.parse("3 A, 1 E => 1 FUEL")).isEqualTo(Reaction(listOf("A", "A", "A", "E"), listOf("FUEL")))
    }

    @Test
    fun `simple test`() {
        val input = """
            10 ORE => 10 A
            1 ORE => 1 B
            7 A, 1 B => 1 C
            7 A, 1 C => 1 D
            7 A, 1 D => 1 E
            7 A, 1 E => 1 FUEL
        """.trimIndent()

        val reactions = input.split("\n")
            .map { Reaction.parse(it) }

        val output = reactions.produce(listOf(FUEL))
        println(output)
        assertThat(output.count { it == "ORE" }).isEqualTo(31)
    }

    @Test
    fun `complex test`() {
        val input = """
            171 ORE => 8 CNZTR
            7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL
            114 ORE => 4 BHXH
            14 VRPVC => 6 BMBT
            6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL
            6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT
            15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW
            13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW
            5 BMBT => 4 WPTQ
            189 ORE => 9 KTJDG
            1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP
            12 VRPVC, 27 CNZTR => 2 XDBXC
            15 KTJDG, 12 BHXH => 5 XCVML
            3 BHXH, 2 VRPVC => 7 MZWV
            121 ORE => 7 VRPVC
            7 XCVML => 6 RJRHP
            5 BHXH, 4 VRPVC => 5 LTCX
        """.trimIndent()

        val reactions = input.split("\n")
            .map { Reaction.parse(it) }

        val output = reactions.produce(listOf(FUEL))
        assertThat(output.count { it == "ORE" }).isEqualTo(2210736)
    }
}