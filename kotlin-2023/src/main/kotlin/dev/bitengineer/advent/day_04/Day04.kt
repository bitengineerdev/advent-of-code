package dev.bitengineer.advent.day_04

import dev.bitengineer.advent.util.assertEquals
import dev.bitengineer.advent.util.input
import dev.bitengineer.advent.util.part1TestFile
import kotlin.math.pow

fun main() {
    val day = "04"
    part1TestFile(day).useLines { input ->
        assertEquals(13, part1(input))
    }
    part1TestFile(day).useLines { input ->
        assertEquals(30, part2(input))
    }

    input(day).useLines { input ->
        println("Part 1: ${part1(input)}")
    }
    input(day).useLines { input ->
        println("Part 2: ${part2(input)}")
    }
}

fun part1(input: Sequence<String>): Int = input
    .map { ScratchCard.from(it).matchingNumbers }
    .filter { it.isNotEmpty() }
    .sumOf { powerOfTwo(it.size - 1) }

fun part2(input: Sequence<String>): Int = processStackOfScratchCards(input).sumOf { it.count }

data class ScratchCard(
    val cardNumber: Int,
    val winningNumbers: Set<Int>,
    val scratchNumbers: Set<Int>
) {
    val matchingNumbers: Set<Int> = winningNumbers.intersect(scratchNumbers)

    companion object {
        fun from(input: String): ScratchCard {
            val values = input.split(": ", " | ", limit = 3)
            val cardNumber = values[0].split(" ", limit = 2)[1].trim().toInt()
            val winningNumbers = values[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()
            val scratchNumbers = values[2].split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()
            return ScratchCard(cardNumber, winningNumbers, scratchNumbers)
        }
    }
}

data class ScratchCardStack(
    val card: ScratchCard,
    val count: Int
)

fun processStackOfScratchCards(input: Sequence<String>): List<ScratchCardStack> {
    val allCards = input.map { ScratchCardStack(ScratchCard.from(it), 1) }
        .associateBy { it.card.cardNumber }
        .toMutableMap()

    allCards.forEach { (cardNumber, cardStack) ->
        val matchingNumbers = cardStack.card.matchingNumbers
        (1..matchingNumbers.size).forEach { index ->
            allCards.compute(cardNumber + index) { _, stack ->
                stack?.copy(count = stack.count + cardStack.count) ?: error("Card not found")
            }
        }
    }
    return allCards.values.toList()
}

fun powerOfTwo(n: Int): Int = 2.toDouble().pow(n).toInt()
