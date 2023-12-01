package dev.bitengineer.advent.util

import java.io.File

/**
 * Reads lines from the given input txt file.
 */
const val PROJECT = "kotlin-2023"
fun readInput(name: String) = File("$PROJECT/src/main/kotlin/dev/bitengineer/advent", name).readLines()

fun readDayTestInput1(day: String) = readInput("day_$day/test_input_1.txt")
fun readDayTestInput2(day: String) = readInput("day_$day/test_input_2.txt")
fun readDayInput(day: String) = readInput("day_$day/input.txt")

fun assertEquals(expected: Any, actual: Any) {
    if (expected != actual) {
        throw AssertionError("Expected $expected but was $actual")
    }
}
