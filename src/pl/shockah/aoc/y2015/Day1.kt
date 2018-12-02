package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day1 : AdventTask<String, Int, Int>(2015, 1) {
	override fun parseInput(rawInput: String): String {
		return rawInput
	}

	override fun part1(input: String): Int {
		return input.length - input.count { it == ')' } * 2
	}

	override fun part2(input: String): Int {
		var current = 0
		for (i in 0 until input.length) {
			current += if (input[i] == '(') 1 else -1
			if (current == -1)
				return i + 1
		}
		throw IllegalArgumentException("Never reaching basement.")
	}

	@Suppress("FunctionName")
	class Tests {
		private val task = Day1()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput("(())")
				Assertions.assertEquals(0, task.part1(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("()()")
				Assertions.assertEquals(0, task.part1(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("(((")
				Assertions.assertEquals(3, task.part1(input))
			}

			@Test
			fun `#4`() {
				val input = task.parseInput("(()(()(")
				Assertions.assertEquals(3, task.part1(input))
			}

			@Test
			fun `#5`() {
				val input = task.parseInput("))(((((")
				Assertions.assertEquals(3, task.part1(input))
			}

			@Test
			fun `#6`() {
				val input = task.parseInput("())")
				Assertions.assertEquals(-1, task.part1(input))
			}

			@Test
			fun `#7`() {
				val input = task.parseInput("))(")
				Assertions.assertEquals(-1, task.part1(input))
			}

			@Test
			fun `#8`() {
				val input = task.parseInput(")))")
				Assertions.assertEquals(-3, task.part1(input))
			}

			@Test
			fun `#9`() {
				val input = task.parseInput(")())())")
				Assertions.assertEquals(-3, task.part1(input))
			}
		}

		@Nested
		inner class Part2 {
			@Test
			fun `#1`() {
				val input = task.parseInput(")")
				Assertions.assertEquals(1, task.part2(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("()())")
				Assertions.assertEquals(5, task.part2(input))
			}
		}
	}
}