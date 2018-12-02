package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day5: AdventTask<List<Int>, Int, Int>(2017, 5) {
	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.lines().map { it.toInt() }
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun task(input: List<Int>, part2Rule: Boolean): Int {
		val instructions = input.toMutableList()
		var index = 0
		var steps = 0

		while (true) {
			if (index !in 0 until instructions.size)
				return steps

			val jump = instructions[index]

			if (part2Rule && jump >= 3)
				instructions[index]--
			else
				instructions[index]++

			index += jump
			steps++
		}
	}

	override fun part1(input: List<Int>): Int {
		return task(input, false)
	}

	override fun part2(input: List<Int>): Int {
		return task(input, true)
	}

	@Suppress("FunctionName")
	class Tests {
		private val task = Day5()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput("0\n3\n0\n1\n-3")
				Assertions.assertEquals(5, task.part1(input))
			}
		}

		@Nested
		inner class Part2 {
			@Test
			fun `#1`() {
				val input = task.parseInput("0\n3\n0\n1\n-3")
				Assertions.assertEquals(10, task.part2(input))
			}
		}
	}
}