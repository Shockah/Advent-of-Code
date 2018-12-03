package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask

class Day1: AdventTask<List<Int>, Int, Int>(2018, 1) {
	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.lines().map { it.toInt() }
	}

	override fun part1(input: List<Int>): Int {
		return input.sum()
	}

	override fun part2(input: List<Int>): Int {
		val set = mutableSetOf(0)
		var current = 0
		while (true) {
			for (change in input) {
				current += change
				if (current in set)
					return current
				set += current
			}
		}
	}

	class Tests {
		private val task = Day1()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(listOf(
				Case("+1\n+1\n+1", 3),
				Case("+1\n+1\n-2", 0),
				Case("-1\n-2\n-3", -6)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(listOf(
				Case("+1\n-1", 0),
				Case("+3\n+3\n+4\n-2\n-4", 10),
				Case("-6\n+3\n+8\n+5\n-6", 5),
				Case("+7\n+7\n-2\n-7\n-4", 14)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}