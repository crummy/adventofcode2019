package com.malcolmcrum.adventofcode2019

fun validPasswords(range: IntRange): Int {
    return range
        .filter { it.digitsNeverDecrease }
        .filter { it.hasDoubleDigits }
        .filter { it.countDigits() == 6 }
        .count()
}

fun Int.countDigits(): Int {
    return when (this) {
        in (0..9) -> 1
        else -> 1 + (this / 10).countDigits()
    }
}

val Int.hasDoubleDigits: Boolean
    get() {
        val digits = this.toString().map(Character::getNumericValue)
        var lastDigit = 0
        for (digit in digits) {
            if (digit == lastDigit) return true
            lastDigit = digit
        }
        return false
    }

val Int.digitsNeverDecrease: Boolean
    get() {
        val digits = this.toString().map(Character::getNumericValue)
        var lastDigit = 0
        for (digit in digits) {
            if (digit < lastDigit) return false
            lastDigit = digit
        }
        return true
    }

fun main() {
    val count = validPasswords(134564..585159)

    println(count)
}