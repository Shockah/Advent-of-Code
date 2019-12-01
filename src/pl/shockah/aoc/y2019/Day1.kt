package pl.shockah.aoc.y2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.aoc.sumByLong

class Day1 : AdventTask<List<Long>, Long, Long>(2019, 1) {
	override fun parseInput(rawInput: String): List<Long> {
		return rawInput.lines().filter { !it.isEmpty() }.map { it.toLong() }
	}

	private fun getCost(mass: Long): Long {
		return mass / 3 - 2
	}

	override fun part1(input: List<Long>): Long {
		return input.sumByLong { getCost(it) }
	}

	override fun part2(input: List<Long>): Long {
		return input.sumByLong {
			var total = 0L
			var current = it
			while (current >= 0) {
				current = getCost(current)
				total += current.coerceAtLeast(0)
			}
			return@sumByLong total
		}
	}

	class Tests {
		private val task = Day1()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				"12" expects 2L,
				"14" expects 2L,
				"1969" expects 654L,
				"100756" expects 33583L
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
				"14" expects 2L,
				"1969" expects 966L,
				"100756" expects 50346L
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}