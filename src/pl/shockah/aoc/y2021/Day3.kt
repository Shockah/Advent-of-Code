package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.collection.Array2D
import pl.shockah.unikorn.collection.takeRows

class Day3: AdventTask<Array2D<Boolean>, Int, Long>(2021, 3) {
	override fun parseInput(rawInput: String): Array2D<Boolean> {
		val lines = rawInput.lines()
		return Array2D(lines[0].length, lines.size) { x, y -> lines[y][x] == '1' }
	}

	override fun part1(input: Array2D<Boolean>): Int {
		val mostCommonBits = (0 until input.width)
			.map { x ->
				(0 until input.height)
					.asSequence()
					.map { y -> input[x, y] }
					.groupBy { it }
					.map { it.value }
					.sortedByDescending { it.size }
					.map { it.first() }
					.first()
			}
		val gammaRate = mostCommonBits.joinToString("") { if (it) "1" else "0" }.toInt(2)
		val epsilonRate = (1 shl input.width) - gammaRate - 1
		return gammaRate * epsilonRate
	}

	override fun part2(input: Array2D<Boolean>): Long {
		fun getCommonBit(index: Int, values: Array2D<Boolean>, ascending: Boolean, onEqual: Boolean): Boolean {
			val unsorted = values
				.getColumn(index)
				.groupBy { it }
				.map { it.value }
			if (unsorted.size == 2 && unsorted[0].size == unsorted[1].size)
				return onEqual
			val sorted = if (ascending) unsorted.sortedBy { it.size } else unsorted.sortedByDescending { it.size }
			return sorted
				.map { it.first() }
				.first()
		}

		var current1 = input
		var current2 = input
		for (index in 0 until input.width) {
			val mostCommonBit = getCommonBit(index, current1, false, onEqual = true)
			val leastCommonBit = getCommonBit(index, current2, true, onEqual = false)
			current1 = if (current1.height > 1) current1.takeRows { it[index] == mostCommonBit } else current1
			current2 = if (current2.height > 1) current2.takeRows { it[index] == leastCommonBit } else current2
		}

		val oxygenRating = current1.getRow(0).joinToString("") { if (it) "1" else "0" }.toInt(2)
		val scrubberRating = current2.getRow(0).joinToString("") { if (it) "1" else "0" }.toInt(2)
		return oxygenRating.toLong() * scrubberRating.toLong()
	}

	class Tests {
		private val task = Day3()

		private val rawInput = """
			00100
			11110
			10110
			10111
			10101
			01111
			00111
			11100
			10000
			11001
			00010
			01010
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(198, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(230, task.part2(input))
		}
	}
}