package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day5: AdventTask<List<Day5.BoardingPass>, Int, Int>(2020, 5) {
	class BoardingPass(
			val bits: BooleanArray
	) {
		val row by lazy { bits.take(7).map { if (it) '1' else '0' }.joinToString("").toInt(2) }
		val column by lazy { bits.drop(7).take(3).map { if (it) '1' else '0' }.joinToString("").toInt(2) }
		val seatId by lazy { row * 8 + column }
	}

	override fun parseInput(rawInput: String): List<BoardingPass> {
		return rawInput.lines().map { BoardingPass(it.map { it == 'B' || it == 'R' }.toBooleanArray()) }
	}

	override fun part1(input: List<BoardingPass>): Int {
		return input.map { it.seatId }.max()!!
	}

	override fun part2(input: List<BoardingPass>): Int {
		val sorted = input.sortedBy { it.seatId }
		for (index in 1 until sorted.size) {
			if (sorted[index].seatId - sorted[index - 1].seatId == 2)
				return sorted[index].seatId - 1
		}
		throw IllegalStateException("Could not find seat")
	}

	class Tests {
		private val task = Day5()

		private val rawInput = """
			BFFFBBFRRR
			FFFBBBFRRR
			BBFFBBFRLL
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(820, task.part1(input))
		}
	}
}