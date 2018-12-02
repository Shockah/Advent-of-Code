package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

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

	@Suppress("FunctionName")
	class Tests {
		private val task = Day1()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput("+1\n+1\n+1")
				Assertions.assertEquals(3, task.part1(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("+1\n+1\n-2")
				Assertions.assertEquals(0, task.part1(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("-1\n-2\n-3")
				Assertions.assertEquals(-6, task.part1(input))
			}
		}

		@Nested
		inner class Part2 {
			@Test
			fun `#1`() {
				val input = task.parseInput("+1\n-1")
				Assertions.assertEquals(0, task.part2(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("+3\n+3\n+4\n-2\n-4")
				Assertions.assertEquals(10, task.part2(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("-6\n+3\n+8\n+5\n-6")
				Assertions.assertEquals(5, task.part2(input))
			}

			@Test
			fun `#4`() {
				val input = task.parseInput("+7\n+7\n-2\n-7\n-4")
				Assertions.assertEquals(14, task.part2(input))
			}
		}
	}
}