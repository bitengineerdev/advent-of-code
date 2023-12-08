package dev.bitengineer.advent.day_05

import dev.bitengineer.advent.util.assertEquals
import dev.bitengineer.advent.util.input
import dev.bitengineer.advent.util.part1TestFile

fun main() {
    val day = "05"
    assertEquals(35L, part1(part1TestFile(day).readText()))
    val resultPart1 = part1(input(day).readText())
    assertEquals(323142486L, resultPart1)
    println("Part 1: $resultPart1")

    assertEquals(46L, part2(part1TestFile(day).readText()))
    val resultPart2 = part2(input(day).readText())
    println("Part 2: $resultPart2")
}

fun part1(input: String): Long {
    val (seeds, categoryMappers) = parse(input, seedsInRanges = false)
    return seeds.flatMap { it.findLocation(categoryMappers) }.minOf { it.first }
}

fun part2(input: String): Long {
    val (seeds, categoryMappers) = parse(input, seedsInRanges = true)
    return seeds.flatMap { it.findLocation(categoryMappers) }.minOf { it.first }
}

private fun LongRange.findLocation(categoryMappers: List<CategoryMapper>): List<LongRange> =
    categoryMappers.fold(listOf(this)) { ranges, categoryMapper ->
        ranges.flatMap { range ->
            categoryMapper.mappers.mapNotNull { mapper -> range.mapTo(mapper) }
                .takeIf { it.isNotEmpty() }
                ?: listOf(range)
        }
    }

private fun LongRange.mapTo(rangeMapper: RangeMapper): LongRange? =
    when {
        rangeMapper.range.first >= this.first && rangeMapper.range.last <= this.last -> {
            // applied range mapper is fully contained in the source range
            rangeMapper.range.shift(rangeMapper.mappedValue)
        }
        this.first >= rangeMapper.range.first && this.last <= rangeMapper.range.last -> {
            // applied range mapper fully contains other range mapper
            this.shift(rangeMapper.mappedValue)
        }
        rangeMapper.range.first < this.last && rangeMapper.range.last > this.last -> {
            // applied range mapper overlaps on the left side
            (rangeMapper.range.first..this.last).shift(rangeMapper.mappedValue)
        }
        rangeMapper.range.first < this.first && rangeMapper.range.last > this.first -> {
            // applied range mapper overlaps on the right side
            (this.first..rangeMapper.range.last).shift(rangeMapper.mappedValue)
        }
        else -> {
            // applied range mapper is completely outside the source range
            null
        }
    }

private fun LongRange.shift(shift: Long): LongRange = (this.first + shift)..(this.last + shift)

private fun parse(input: String, seedsInRanges: Boolean): Almanac {
    val blocks = input.split("\n\n")
    val seedBlock = blocks.first().split(" ").mapNotNull { it.toLongOrNull() }
    val seeds = if (seedsInRanges) {
        seedBlock.chunked(2).map { it.first().until(it.first() + it.last()) }
    } else {
        seedBlock.map { it..it }
    }
    val rangeMappers = blocks.drop(1).map { CategoryMapper.from(it) }

    return Almanac(seeds, rangeMappers)
}

private data class Almanac(
    val seeds: List<LongRange>,
    val categoryMappers: List<CategoryMapper>
)

private data class CategoryMapper(
    val mappers: List<RangeMapper>
) {
    companion object {
        fun from(block: String): CategoryMapper {
            return CategoryMapper(block.split("\n").drop(1).map { RangeMapper.from(it) })
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
