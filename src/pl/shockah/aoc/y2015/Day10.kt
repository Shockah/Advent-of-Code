package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

class Day10: AdventTask<String, Int, Int>(2015, 10) {
	override fun parseInput(rawInput: String): String {
		return rawInput
	}

	private fun process(input: String): String {
		var last: Char? = null
		var count = 0
		val builder = StringBuilder()

		for (c in input) {
			if (last != null && last != c) {
				builder.append(count)
				builder.append(last)
				count = 0
			}
			last = c
			count++
		}

		builder.append(count)
		builder.append(last)
		return builder.toString()
	}

	private fun task(input: String, times: Int): Int {
		var current = input
		repeat(times) {
			current = process(current)
		}
		return current.length
	}

	override fun part1(input: String): Int {
		return task(input, 40)
	}

	override fun part2(input: String): Int {
		return task(input, 50)
	}

	class Tests {
		private val task = Day10()

		@TestFactory
		fun process(): Collection<DynamicTest> = createTestCases(
				"1" expects "11",
				"11" expects "21",
				"21" expects "1211",
				"1211" expects "111221",
				"111221" expects "312211"
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.process(input))
		}
	}
}