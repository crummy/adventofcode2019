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
}