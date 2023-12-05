package dev.bitengineer.advent.day_02

import dev.bitengineer.advent.util.assertEquals
import dev.bitengineer.advent.util.input
import dev.bitengineer.advent.util.part1TestFile

fun main() {
    val day = "02"
    part1TestFile(day).useLines { input ->
        assertEquals(8, part1(input))
    }
    part1TestFile(day).useLines { input ->
        assertEquals(2286, part2(input))
    }

    input(day).useLines { input ->
        println("Part 1: ${part1(input)}")
    }
    input(day).useLines { input ->
        println("Part 2: ${part2(input)}")
    }
}

fun part1(input: Sequence<String>): Int {
    val totalCubes = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14
    )

    return input.mapIndexed { index: Int, game: String ->
        if (game.stripGameId().isValidGame(totalCubes)) {
            index + 1
        } else 0
    }.sum()
}

fun String.stripGameId(): String = this.split(": ", limit = 2)[1]

fun String.isValidGame(totalCubes: Map<String, Int>): Boolean = this.split("; ").all { attempt ->
    attempt.split(", ", limit = 3).map { it.trim() }
        .all {
            it.split(" ", limit = 2).let { (count, color) ->
                totalCubes.getOrDefault(color, 0) >= count.toInt()
            }
        }

}

fun part2(input: Sequence<String>): Int =
    input.map { it.stripGameId().minimumCubes() }
        .sumOf {
            it.values.reduce { acc, count ->
                acc * count
            }
        }

fun String.minimumCubes(): Map<String, Int> =
    this.split("; ").map { attempt ->
        attempt.split(", ", limit = 3).map { it.trim() }.associate {
            it.split(" ", limit = 2).let { (count, color) ->
                color to count.toInt()
            }
        }
    }.reduce { acc, map ->
        acc.mapValues { (color, count) ->
            maxOf(count, map[color] ?: 0)
        } + map.filterKeys { !acc.containsKey(it) }
    }
