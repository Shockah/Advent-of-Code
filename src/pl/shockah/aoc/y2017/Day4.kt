package pl.shockah.aoc.y2017

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.createRawPart1TestCases
import pl.shockah.aoc.createRawPart2TestCases
import pl.shockah.aoc.expects

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
		fun part1(): Collection<DynamicTest> = task.createRawPart1TestCases(
			"aa bb cc dd ee" expects 1,
			"aa bb cc dd aa" expects 0,
			"aa bb cc dd aaa" expects 1
		)

		@TestFactory
		fun part2(): Collection<DynamicTest> = task.createRawPart2TestCases(
			"abcde fghij" expects 1,
			"abcde xyz ecdab" expects 0,
			"a ab abc abd abf abj" expects 1,
			"iiii oiii ooii oooi oooo" expects 1,
			"oiii ioii iioi iiio" expects 0
		)
	}
}