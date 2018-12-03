package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask

class Day4: AdventTask<List<List<String>>, Int, Int>(2017, 4) {
	override fun parseInput(rawInput: String): List<List<String>> {
		return rawInput.lines().map {
			it.trim().split(inputSplitPattern)
		}
	}

	override fun part1(input: List<List<String>>): Int {
		return input.filter {
			setOf(*it.toTypedArray()).size == it.size
		}.size
	}

	override fun part2(input: List<List<String>>): Int {
		return input.filter {
			setOf(*it.map {
				it.toCharArray().sortedArray().contentToString()
			}.toTypedArray()).size == it.size
		}.size
	}

	class Tests {
		private val task = Day4()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(listOf(
				Case("aa bb cc dd ee", 1),
				Case("aa bb cc dd aa", 0),
				Case("aa bb cc dd aaa", 1)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(listOf(
				Case("abcde fghij", 1),
				Case("abcde xyz ecdab", 0),
				Case("a ab abc abd abf abj", 1),
				Case("iiii oiii ooii oooi oooo", 1),
				Case("oiii ioii iioi iiio", 0)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}