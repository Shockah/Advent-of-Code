package pl.shockah.aoc.y2018

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.createRawPart1TestCases
import pl.shockah.aoc.createRawPart2TestCases
import pl.shockah.aoc.expects

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
		fun part1(): Collection<DynamicTest> = task.createRawPart1TestCases(
			"+1\n+1\n+1" expects 3,
			"+1\n+1\n-2" expects 0,
			"-1\n-2\n-3" expects -6
		)

		@TestFactory
		fun part2(): Collection<DynamicTest> = task.createRawPart2TestCases(
			"+1\n-1" expects 0,
			"+3\n+3\n+4\n-2\n-4" expects 10,
			"-6\n+3\n+8\n+5\n-6" expects 5,
			"+7\n+7\n-2\n-7\n-4" expects 14
		)
	}
}