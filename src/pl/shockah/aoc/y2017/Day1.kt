package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
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

	class Tests {
		private val task = Day1()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(listOf(
				Case("1122", 3),
				Case("1111", 4),
				Case("1234", 0),
				Case("91212129", 9)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(listOf(
				Case("1212", 6),
				Case("1221", 0),
				Case("123425", 4),
				Case("123123", 12),
				Case("12131415", 4)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}