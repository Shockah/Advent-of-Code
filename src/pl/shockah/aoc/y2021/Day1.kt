package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day1: AdventTask<List<Int>, Int, Int>(2021, 1) {
	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.trim().lines().map { it.toInt() }
	}

	private fun task(input: List<Int>): Int {
		return input.windowed(2).count { (a, b) -> a < b }
	}

	override fun part1(input: List<Int>): Int {
		return task(input)
	}

	override fun part2(input: List<Int>): Int {
		return task(input.windowed(3).map { it.sum() })
	}

	class Tests {
		private val task = Day1()

		private val rawInput = """
			199
			200
			208
			210
			200
			207
			240
			269
			260
			263
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(7, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(5, task.part2(input))
		}
	}
}