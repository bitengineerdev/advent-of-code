package dev.bitengineer.advent.day_03

import dev.bitengineer.advent.util.assertEquals
import dev.bitengineer.advent.util.input
import dev.bitengineer.advent.util.part1TestFile

fun main() {
    val day = "03"
    part1TestFile(day).useLines { input ->
        assertEquals(4361, part1(input))
    }
    part1TestFile(day).useLines { input ->
        assertEquals(467835, part2(input))
    }

    input(day).useLines { input ->
        println("Part 1: ${part1(input)}")
    }
    input(day).useLines { input ->
        println("Part 2: ${part2(input)}")
    }
}

fun part1(input: Sequence<String>): Int = EngineSchematic.from(input).findPartNumbers().sumOf { it.partNumber }

fun part2(input: Sequence<String>): Int = EngineSchematic.from(input).findAllGearRatios().sum()

data class EngineSchematicNumber(
    val partNumber: Int,
    val positions: List<Pair<Int, Int>>
)

data class EngineSchematicSymbol(
    val position: Pair<Int, Int>,
    val symbol: Char
)

data class EngineSchematic(
    private val numbers: List<EngineSchematicNumber>,
    private val symbols: List<EngineSchematicSymbol>
) {

    private val numbersByPosition: Map<Pair<Int, Int>, EngineSchematicNumber> =
        numbers
            .flatMap { part ->
                part.positions.map { it to part }
            }.toMap()

    fun findPartNumbers(): Set<EngineSchematicNumber> =
        symbols.fold(emptySet()) { parts, symbol ->
            parts + symbol.associatedNumbers()
        }

    fun findAllGearRatios(): List<Int> =
        symbols.mapNotNull { symbol ->
            val surroundingNumbers = symbol.associatedNumbers()
            if (symbol.symbol == '*' && surroundingNumbers.size == 2) {
                surroundingNumbers[0].partNumber * surroundingNumbers[1].partNumber
            } else {
                null
            }
        }

    private fun EngineSchematicSymbol.associatedNumbers(): List<EngineSchematicNumber> =
        this.position.surroundingPositions().mapNotNull { numbersByPosition[it] }.distinct()

    private fun Pair<Int, Int>.surroundingPositions(): List<Pair<Int, Int>> =
        listOf(
            this.first + 1 to this.second,
            this.first - 1 to this.second,
            this.first to this.second + 1,
            this.first to this.second - 1,
            this.first + 1 to this.second + 1,
            this.first + 1 to this.second - 1,
            this.first - 1 to this.second + 1,
            this.first - 1 to this.second - 1
        )

    companion object {
        fun from(input: Sequence<String>): EngineSchematic {
            val parsedValues = input.mapIndexed { line, row -> parseRow(row, line) }.toList()
            return EngineSchematic(
                numbers = parsedValues.flatMap { it.first },
                symbols = parsedValues.flatMap { it.second }
            )
        }

        private fun parseRow(row: String, line: Int): Pair<Set<EngineSchematicNumber>, Set<EngineSchematicSymbol>> {
            val numbers = mutableSetOf<EngineSchematicNumber>()
            val symbols = mutableSetOf<EngineSchematicSymbol>()
            var index = 0
            while (index < row.length) {
                if (row[index].isDigit()) {
                    val endPos = findNumberEndPosition(row, index)
                    numbers += EngineSchematicNumber(
                        partNumber = row.substring(index, endPos + 1).toInt(),
                        positions = determinePositions(line, index, endPos)
                    )
                    index = endPos
                } else if (row[index] != '.') {
                    symbols += EngineSchematicSymbol(
                        position = index to line,
                        symbol = row[index]
                    )
                }
                index++
            }
            return numbers to symbols
        }

        private fun findNumberEndPosition(input: String, startPos: Int): Int {
            var pos = startPos + 1
            while (pos < input.length && input[pos].isDigit()) {
                pos++
            }
            return pos - 1
        }

        private fun determinePositions(line: Int, startPos: Int, endPos: Int): List<Pair<Int, Int>> =
            (startPos..endPos).map { it to line }
    }
}
