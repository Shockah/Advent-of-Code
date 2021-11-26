package pl.shockah.aoc.y2015

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.createRawPart1TestCases
import pl.shockah.aoc.createRawPart2TestCases
import pl.shockah.aoc.expects

class Day5: AdventTask<List<String>, Int, Int>(2015, 5) {
	override fun parseInput(rawInput: String): List<String> {
		return rawInput.lines()
	}

	override fun part1(input: List<String>): Int {
		val vowels = "aeiou"
		val bannedWords = listOf("ab", "cd", "pq", "xy")

		return input.filter {
			if (it.toCharArray().filter { it in vowels }.size < 3)
				return@filter false

			for (bannedWord in bannedWords) {
				if (bannedWord in it)
					return@filter false
			}

			var hasInARow = false
			for (i in 1 until it.length) {
				if (it[i] == it[i - 1]) {
					hasInARow = true
					break
				}
			}

			return@filter hasInARow
		}.size
	}

	override fun part2(input: List<String>): Int {
		return input.filter {
			var hasInARow = false
			for (i in 2 until it.length) {
				if (it[i] == it[i - 2]) {
					hasInARow = true
					break
				}
			}

			if (!hasInARow)
				return@filter false

			val pairSet = mutableSetOf<String>()
			for (i in 1 until it.length) {
				pairSet += it.substring(i - 1, i + 1)
			}

			for (pair in pairSet) {
				val firstIndex = it.indexOf(pair)
				if (it.indexOf(pair, firstIndex + 2) != -1)
					return@filter true
			}

			return@filter false
		}.size
	}

	class Tests {
		private val task = Day5()

		@TestFactory
		fun part1(): Collection<DynamicTest> = task.createRawPart1TestCases(
			"ugknbfddgicrmopn" expects 1,
			"aaa" expects 1,
			"jchzalrnumimnmhp" expects 0,
			"haegwjzuvuyypxyu" expects 0,
			"dvszwmarrgswjxmb" expects 0
		)

		@TestFactory
		fun part2(): Collection<DynamicTest> = task.createRawPart2TestCases(
			"qjhvhtzxzqqjkmpb" expects 1,
			"xxyxx" expects 1,
			"uurcxstgmygtbstg" expects 0,
			"ieodomkazucvgmuy" expects 0
		)
	}
}