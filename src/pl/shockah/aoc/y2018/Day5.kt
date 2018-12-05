package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

class Day5 : AdventTask<String, Int, Int>(2018, 5) {
	override fun parseInput(rawInput: String): String {
		return rawInput
	}

	private fun react(input: String): Int {
		var current = input
		while (true) {
			val oldLength = current.length

			var i = 1
			while (i < current.length) {
				val c1 = current[i - 1]
				val c2 = current[i]
				if (c1.toLowerCase() == c2.toLowerCase() && c1.isLowerCase() != c2.isLowerCase()) {
					current = current.removeRange(i - 1, i + 1)
					continue
				}
				i++
			}

			if (oldLength == current.length)
				return current.length
		}
	}

	override fun part1(input: String): Int {
		return react(input)
	}

	override fun part2(input: String): Int {
		val polymerTypes = input.toLowerCase().toCharArray().toSet()
		return polymerTypes.map {
			return@map react(input.replace(it.toString(), "").replace(it.toUpperCase().toString(), ""))
		}.min()!!
	}

	class Tests {
		private val task = Day5()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				"aA" expects 0,
				"abBA" expects 0,
				"abAB" expects 4,
				"aabAAB" expects 6,
				"dabAcCaCBAcCcaDA" expects 10
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput("dabAcCaCBAcCcaDA")
			Assertions.assertEquals(4, task.part2(input))
		}
	}
}