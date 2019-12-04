package com.malcolmcrum.adventofcode2019

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

internal class Day4Test {
    @Test
    fun `return single valid password`() {
        val password = validPasswords(111111..111111)

        assertThat(password).isEqualTo(1)
    }

    @Test
    fun `digits never decrease`() {
        assertThat(111.digitsNeverDecrease).isTrue()
        assertThat(112.digitsNeverDecrease).isTrue()
        assertThat(110.digitsNeverDecrease).isFalse()
    }

    @Test
    fun `count digits`() {
        assertThat(111.countDigits() == 3)
        assertThat(1.countDigits() == 1)
        assertThat(55994.countDigits() == 5)
    }

    @Test
    fun `has adjacent duplicate digits`() {
        assertThat(55.hasDoubleDigits).isTrue()
        assertThat(555.hasDoubleDigits).isTrue()
        assertThat(515.hasDoubleDigits).isFalse()
        assertThat(515151515.hasDoubleDigits).isFalse()
        assertThat(515661515.hasDoubleDigits).isTrue()
    }
}
