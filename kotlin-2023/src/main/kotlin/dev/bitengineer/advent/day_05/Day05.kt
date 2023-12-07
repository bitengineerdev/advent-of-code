package dev.bitengineer.advent.day_05

import dev.bitengineer.advent.util.assertEquals
import dev.bitengineer.advent.util.input
import dev.bitengineer.advent.util.part1TestFile

fun main() {
    val day = "05"
    assertEquals(35L, part1(part1TestFile(day).readText()))
    // part1TestFile(day).useLines { input ->
    //     assertEquals(30, part2(input))
    // }

    println("Part 1: ${part1(input(day).readText())}")
    // input(day).useLines { input ->
    //     println("Part 2: ${part2(input)}")
    // }
}

fun part1(input: String): Long {
    val blocks = input.split("\n\n")
    val seeds = blocks.first().split(" ").mapNotNull { it.toLongOrNull() }
    val categoryMappers = blocks.drop(1).map { CategoryMapper.from(it.split("\n")) }
        .associateBy { it.source }

    return seeds.minOf { categoryMappers.findLocationFromSeed(it) }
}

fun part2(input: List<String>): Int = TODO()

private data class CategoryMapper(
    val source: String,
    val destination: String,
    val mappedValues: List<RangeMapper>
) {
    fun map(value: Long): Long = value + (mappedValues.firstOrNull { it.range.contains(value) }?.mappedValue ?: 0L)

    companion object {
        fun from(block: List<String>): CategoryMapper {
            val (source, destination) = block.first().removeSuffix(" map:").split("-to-", " ", limit = 2)
            val mappers = block.drop(1).map { RangeMapper.from(it) }
            return CategoryMapper(source, destination, mappers)
        }
    }
}

private data class RangeMapper(
    val range: LongRange,
    val mappedValue: Long
) {
    companion object {
        fun from(input: String): RangeMapper {
            val (destination, source, range) = input.split(" ", limit = 3).map { it.toLong() }
            return RangeMapper(source.until(source + range), destination - source)
        }
    }
}

private fun Map<String, CategoryMapper>.findLocationFromSeed(seed: Long): Long {
    var category = this["seed"] ?: error("No category found for seed")
    var value = seed
    while(true) {
        println("${category.source} $value -> ${category.destination} -> ${category.map(value)}")
        value = category.map(value)
        if (category.destination == "location") {
            return value
        } else {
            category = this[category.destination] ?: error("No category found for ${category.destination}")
        }
    }
}
