package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day14Test {
    @Test
    fun `test parsing`() {
        assertThat(Chemical.parse("2 ORE => 2 A")).isEqualTo(Chemical("A", 2, listOf(Produces("ORE", 2))))
        assertThat(Chemical.parse("3 A, 1 E => 1 FUEL")).isEqualTo(Chemical("FUEL", 1, listOf(Produces("A", 3), Produces("E", 1))))
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

        val chemicals = parseReactions(input.split("\n"))

        val output = chemicals.synthesizeFuel()
        assertThat(output).isEqualTo(31)
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

        val chemicals = parseReactions(input.split("\n"))

        val output = chemicals.synthesizeFuel()
        assertThat(output).isEqualTo(2210736)
    }

    @Test
    fun `test medium sample`() {
        val input = """
            157 ORE => 5 NZVS
            165 ORE => 6 DCFZ
            44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
            12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
            179 ORE => 7 PSHF
            177 ORE => 5 HKGWZ
            7 DCFZ, 7 PSHF => 2 XJWVT
            165 ORE => 2 GPVTF
            3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT
        """.trimIndent()

        val chemicals = parseReactions(input.split("\n"))

        val output = chemicals.synthesizeFuel()
        assertThat(output).isEqualTo(13312)

        val fuel = chemicals.fuelGivenOre(1000000000000)

        assertThat(fuel).isEqualTo(82892753)
    }
}