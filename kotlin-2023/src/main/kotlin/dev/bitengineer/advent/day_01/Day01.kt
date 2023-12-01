package dev.bitengineer.advent.day_01

import dev.bitengineer.advent.util.assertEquals
import dev.bitengineer.advent.util.readDayInput
import dev.bitengineer.advent.util.readDayTestInput1
import dev.bitengineer.advent.util.readDayTestInput2

fun main() {
    val day = "01"
    val testInput1 = readDayTestInput1(day)
    assertEquals(142, part1(testInput1))
    val testInput2 = readDayTestInput2(day)
    assertEquals(281, part2(testInput2))

    val input = readDayInput(day)
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int = input.sumOf { it.findFirstAndLastNumberChar() }

fun part2(input: List<String>): Int = input.sumOf { it.findFirstAndLastNumberCharOrWord() }

private fun String.findFirstAndLastNumberChar(): Int {
    val firstDigit = first { it.isDigit() }
    val lastDigit = last { it.isDigit() }
    return formNumber(firstDigit.toString(), lastDigit.toString())
}

private fun String.findFirstAndLastNumberCharOrWord(): Int {
    val firstDigit = findNumber(0, reversed = false) ?: error("No first digit found in $this")
    val lastDigit = reversed().findNumber(0, reversed = true) ?: error("No last digit found in $this")

    val number = formNumber(firstDigit.toString(), lastDigit.toString())
    println("Number: $number ($firstDigit, $lastDigit) in $this")
    return number
}

private fun formNumber(first: String, last: String): Int = (first + last).toInt()

private fun String.findNumber(startPos: Int, reversed: Boolean): Int? {
    if (this[startPos].isDigit()) return this[startPos].toString().toInt()

    this.substring(startPos).let { subWord ->
        subWord.forEachIndexed { index, c ->
            if (c.isDigit()) return@forEachIndexed
            val wordNumber = subWord.substring(0, index + 1)
                .let { if (reversed) it.reversed() else it }
                .asNumberWordOrNull()
            if (wordNumber != null) {
                return wordNumber
            }
        }
    }

    return this.findNumber(startPos + 1, reversed)
}

private fun String.asNumberWordOrNull(): Int? =
    when (this) {
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        else -> null
    }
