package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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

	@Suppress("FunctionName")
	class Tests {
		private val task = Day5()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput("ugknbfddgicrmopn")
				Assertions.assertEquals(1, task.part1(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("aaa")
				Assertions.assertEquals(1, task.part1(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("jchzalrnumimnmhp")
				Assertions.assertEquals(0, task.part1(input))
			}

			@Test
			fun `#4`() {
				val input = task.parseInput("haegwjzuvuyypxyu")
				Assertions.assertEquals(0, task.part1(input))
			}

			@Test
			fun `#5`() {
				val input = task.parseInput("dvszwmarrgswjxmb")
				Assertions.assertEquals(0, task.part1(input))
			}
		}

		@Nested
		inner class Part2 {
			@Test
			fun `#1`() {
				val input = task.parseInput("qjhvhtzxzqqjkmpb")
				Assertions.assertEquals(1, task.part2(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("xxyxx")
				Assertions.assertEquals(1, task.part2(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("uurcxstgmygtbstg")
				Assertions.assertEquals(0, task.part2(input))
			}

			@Test
			fun `#4`() {
				val input = task.parseInput("ieodomkazucvgmuy")
				Assertions.assertEquals(0, task.part2(input))
			}
		}
	}
}