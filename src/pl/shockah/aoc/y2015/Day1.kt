package pl.shockah.aoc.y2015

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

class Day1: AdventTask<String, Int, Int>(2015, 1) {
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
		fun part1(): Collection<DynamicTest> = createSimpleTestCases(
				"(())" expects 0,
				"()()" expects 0,
				"(((" expects 3,
				"(()(()(" expects 3,
				"))(((((" expects 3,
				"())" expects -1,
				"))(" expects -1,
				")))" expects -3,
				")())())" expects -3
		) { task.part1(task.parseInput(it)) }

		@TestFactory
		fun part2(): Collection<DynamicTest> = createSimpleTestCases(
				")" expects 1,
				"()())" expects 5
		) { task.part2(task.parseInput(it)) }
	}
}