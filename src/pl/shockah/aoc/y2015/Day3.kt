package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask

class Day3 : AdventTask<List<Day3.Direction>, Int, Int>(2015, 3) {
	enum class Direction(
			val symbol: Char,
			val x: Int,
			val y: Int
	) {
		Right('>', 1, 0),
		Up('^', 0, -1),
		Left('<', -1, 0),
		Down('v', 0, 1)
	}

	override fun parseInput(rawInput: String): List<Direction> {
		return rawInput.toCharArray().map { char -> Direction.values().first { it.symbol == char } }
	}

	private fun task(input: List<Direction>, santaCount: Int = 1): Int {
		val santas = Array(santaCount) { Pair(0, 0) }
		val visited = mutableSetOf(Pair(0, 0))
		var santaIndex = 0

		for (direction in input) {
			santas[santaIndex] = Pair(
					santas[santaIndex].first + direction.x,
					santas[santaIndex].second + direction.y
			)
			visited += santas[santaIndex]
			santaIndex = (santaIndex + 1) % santaCount
		}

		return visited.size
	}

	override fun part1(input: List<Direction>): Int {
		return task(input, 1)
	}

	override fun part2(input: List<Direction>): Int {
		return task(input, 2)
	}

	class Tests {
		private val task = Day3()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(listOf(
				Case(">", 2),
				Case("^>v<", 4),
				Case("^v^v^v^v^v", 2)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(listOf(
				Case("^v", 3),
				Case("^>v<", 3),
				Case("^v^v^v^v^v", 11)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}