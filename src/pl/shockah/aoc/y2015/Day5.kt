package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask

class Day5 : AdventTask<List<String>, Int, Int>(2015, 5) {
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
		fun part1(): Collection<DynamicTest> = createTestCases(listOf(
				Case("ugknbfddgicrmopn", 1),
				Case("aaa", 1),
				Case("jchzalrnumimnmhp", 0),
				Case("haegwjzuvuyypxyu", 0),
				Case("dvszwmarrgswjxmb", 0)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(listOf(
				Case("qjhvhtzxzqqjkmpb", 1),
				Case("xxyxx", 1),
				Case("uurcxstgmygtbstg", 0),
				Case("ieodomkazucvgmuy", 0)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}