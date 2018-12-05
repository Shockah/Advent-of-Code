package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

class Day1 : AdventTask<String, Int, Int>(2015, 1) {
	override fun parseInput(rawInput: String): String {
		return rawInput
	}

	override fun part1(input: String): Int {
		return input.length - input.count { it == ')' } * 2
	}

	override fun part2(input: String): Int {
		var current = 0
		input.forEachIndexed { index, c ->
			current += if (c == '(') 1 else -1
			if (current == -1)
				return index + 1
		}
		throw IllegalArgumentException("Never reaching basement.")
	}

	class Tests {
		private val task = Day1()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				"(())" expects 0,
				"()()" expects 0,
				"(((" expects 3,
				"(()(()(" expects 3,
				"))(((((" expects 3,
				"())" expects -1,
				"))(" expects -1,
				")))" expects -3,
				")())())" expects -3
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
				")" expects 1,
				"()())" expects 5
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}