package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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

	@Suppress("FunctionName")
	class Tests {
		private val task = Day4()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput("aa bb cc dd ee")
				Assertions.assertEquals(1, task.part1(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("aa bb cc dd aa")
				Assertions.assertEquals(0, task.part1(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("aa bb cc dd aaa")
				Assertions.assertEquals(1, task.part1(input))
			}
		}

		@Nested
		inner class Part2 {
			@Test
			fun `#1`() {
				val input = task.parseInput("abcde fghij")
				Assertions.assertEquals(1, task.part2(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("abcde xyz ecdab")
				Assertions.assertEquals(0, task.part2(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("a ab abc abd abf abj")
				Assertions.assertEquals(1, task.part2(input))
			}

			@Test
			fun `#4`() {
				val input = task.parseInput("iiii oiii ooii oooi oooo")
				Assertions.assertEquals(1, task.part2(input))
			}

			@Test
			fun `#5`() {
				val input = task.parseInput("oiii ioii iioi iiio")
				Assertions.assertEquals(0, task.part2(input))
			}
		}
	}
}