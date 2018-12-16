package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

class Day14: AdventTask<String, String, Int>(2018, 14) {
	override fun parseInput(rawInput: String): String {
		return rawInput
	}

	override fun part1(input: String): String {
		val intInput = input.toInt()
		val scores = mutableListOf(3, 7)
		val elves = arrayOf(0, 1)

		while (scores.size < intInput + 10) {
			val sum = elves.map { scores[it] }.sum()
			scores += "$sum".toCharArray().map { it - '0' }

			for (elfIndex in 0 until elves.size) {
				elves[elfIndex] = (elves[elfIndex] + 1 + scores[elves[elfIndex]]) % scores.size
			}
		}

		return scores.subList(intInput, intInput + 10).map { '0' + it }.joinToString("")
	}

	override fun part2(input: String): Int {
		val scores = StringBuilder("37")
		val elves = arrayOf(0, 1)

		var lastIndex = 0

		while (true) {
			val sum = elves.map { scores[it] - '0' }.sum()
			scores.append("$sum")

			for (elfIndex in 0 until elves.size) {
				elves[elfIndex] = (elves[elfIndex] + 1 + (scores[elves[elfIndex]] - '0')) % scores.length
			}

			val index = scores.indexOf(input, lastIndex - input.length)
			if (index != -1)
				return index
			lastIndex = scores.length
		}
	}

	class Tests {
		private val task = Day14()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				"9" expects "5158916779",
				"5" expects "0124515891",
				"18" expects "9251071085",
				"2018" expects "5941429882"
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
				"51589" expects 9,
				"01245" expects 5,
				"92510" expects 18,
				"59414" expects 2018
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}