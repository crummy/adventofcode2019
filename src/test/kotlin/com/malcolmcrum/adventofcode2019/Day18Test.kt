package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class Day18Test {
    @Test
    fun `test simple test`() {
        val input = """
            #########
            #b.A.@.a#
            #########
        """.trimIndent()

        val vault = Vault.parse(input)

        assertThat(vault.collectAllKeys()).isEqualTo(8)
    }

    @Test
    fun `test with non-trivial path`() {
        val input = """
            ########################
            #f.D.E.e.C.b.A.@.a.B.c.#
            ######################.#
            #d.....................#
            ########################
        """.trimIndent()

        val vault = Vault.parse(input)

        assertThat(vault.collectAllKeys()).isEqualTo(86)
    }

    @Test
    fun `test with complex path`() {
        val input = """
            #################
            #i.G..c...e..H.p#
            ########.########
            #j.A..b...f..D.o#
            ########@########
            #k.E..a...g..B.n#
            ########.########
            #l.F..d...h..C.m#
            #################
        """.trimIndent()

        val vault = Vault.parse(input)

        assertThat(vault.collectAllKeys()).isEqualTo(136)
    }
}