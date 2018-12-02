package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import kotlin.streams.toList

class Day1 : AdventTask<List<Int>, Int, Int>(2017, 1) {
	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.chars().map { it - zeroAscii }.toList()
	}

	private enum class Mode {
		Next, HalfwayThrough
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun task(input: List<Int>, mode: Mode): Int {
		var sum = 0
		for (i in 0 until input.size) {
			val indexToCheck = when (mode) {
				Mode.Next -> i + 1
				Mode.HalfwayThrough -> i + input.size / 2
			}
			if (input[i] == input[indexToCheck % input.size])
				sum += input[i]
		}
		return sum
	}

	override fun part1(input: List<Int>): Int {
		return task(input, Mode.Next)
	}

	override fun part2(input: List<Int>): Int {
		return task(input, Mode.HalfwayThrough)
	}

	@Suppress("FunctionName")
	class Tests {
		private val task = Day1()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput("1122")
				Assertions.assertEquals(3, task.part1(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("1111")
				Assertions.assertEquals(4, task.part1(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("1234")
				Assertions.assertEquals(0, task.part1(input))
			}

			@Test
			fun `#4`() {
				val input = task.parseInput("91212129")
				Assertions.assertEquals(9, task.part1(input))
			}
		}

		@Nested
		inner class Part2 {
			@Test
			fun `#1`() {
				val input = task.parseInput("1212")
				Assertions.assertEquals(6, task.part2(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("1221")
				Assertions.assertEquals(0, task.part2(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("123425")
				Assertions.assertEquals(4, task.part2(input))
			}

			@Test
			fun `#4`() {
				val input = task.parseInput("123123")
				Assertions.assertEquals(12, task.part2(input))
			}

			@Test
			fun `#5`() {
				val input = task.parseInput("12131415")
				Assertions.assertEquals(4, task.part2(input))
			}
		}
	}
}