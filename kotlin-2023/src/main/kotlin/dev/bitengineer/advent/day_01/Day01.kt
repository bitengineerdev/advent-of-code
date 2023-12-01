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

private val maxNumberWordLength: Int = numberWords.keys.maxOf { it.length }
private val numberWordStartingLettings: Set<Char> = numberWords.keys.map { it.first() }.toSet()

private fun String.calibrationValueByWordOrNumber(): Int {
    val firstDigit = findNumber(reversed = false) ?: error("No first digit found in $this")
    val lastDigit = reversed().findNumber(reversed = true) ?: error("No last digit found in $this")

    return formNumber(firstDigit, lastDigit)
}

private fun formNumber(first: Int, second: Int): Int = (first * 10) + second

private fun String.findNumber(reversed: Boolean): Int? {
    val startChar = this.first()
    if (startChar.isDigit()) return startChar.digitToInt()

    if (startChar in numberWordStartingLettings) {
        forEachIndexed { index, c ->
            if (c.isDigit() || index > maxNumberWordLength) return@forEachIndexed
            val wordNumber = substring(0, index + 1)
                .let { if (reversed) it.reversed() else it }
                .let { numberWords[it] }
            if (wordNumber != null) {
                return wordNumber
            }
        }
    }

    return substring(1).findNumber(reversed)
}

/*
 * Another solution is to use a sequence of digits and then check if the substring starts with a number word.
 */