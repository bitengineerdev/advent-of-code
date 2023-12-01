package dev.bitengineer.advent.day_01

import dev.bitengineer.advent.util.assertEquals
import dev.bitengineer.advent.util.input
import dev.bitengineer.advent.util.part1TestFile
import dev.bitengineer.advent.util.part2TestFile

fun main() {
    val day = "01"
    part1TestFile(day).useLines { input ->
        assertEquals(142, part1(input))
    }
    part2TestFile(day).useLines { input ->
        assertEquals(281, part2(input))
    }

    input(day).useLines { input ->
        println("Part 1: ${part1(input)}")
    }
    input(day).useLines { input ->
        println("Part 2: ${part2(input)}")
    }
}

fun part1(input: Sequence<String>): Int = input.sumOf { it.calibrationValueByDigit() }

fun part2(input: Sequence<String>): Int = input.sumOf { it.calibrationValueByWordOrNumber() }

private fun String.calibrationValueByDigit(): Int {
    val firstDigit = first { it.isDigit() }.digitToInt()
    val lastDigit = last { it.isDigit() }.digitToInt()
    return formNumber(firstDigit, lastDigit)
}

private fun String.calibrationValueByWordOrNumber(): Int {
    val firstDigit = findNumber(0, reversed = false) ?: error("No first digit found in $this")
    val lastDigit = reversed().findNumber(0, reversed = true) ?: error("No last digit found in $this")

    return formNumber(firstDigit, lastDigit)
}

private fun formNumber(first: Int, second: Int): Int = (first * 10) + second

private fun String.findNumber(startPos: Int, reversed: Boolean): Int? {
    if (this[startPos].isDigit()) return this[startPos].toString().toInt()

    this.substring(startPos).let { subWord ->
        subWord.forEachIndexed { index, c ->
            if (c.isDigit()) return@forEachIndexed
            val wordNumber = subWord.substring(0, index + 1)
                .let { if (reversed) it.reversed() else it }
                .let { numberWords[it] }
            if (wordNumber != null) {
                return wordNumber
            }
        }
    }

    return this.findNumber(startPos + 1, reversed)
}

private val numberWords = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)
