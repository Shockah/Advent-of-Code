package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

class Day9: AdventTask<List<Long>, Long, Unit>(2020, 9) {
	override fun parseInput(rawInput: String): List<Long> {
		return rawInput.lines().filter { it.isNotEmpty() }.map { it.toLong() }
	}

	fun isValid(input: List<Long>, index: Int, inputsToCheck: Int): Boolean {
		val sorted = input.subList(index - inputsToCheck, index).distinct().sorted()
		for (i in sorted.indices) {
			for (j in (i + 1) until sorted.size) {
				if (sorted[i] + sorted[j] == input[index])
					return true
			}
		}
		return false
	}

	fun part1(input: List<Long>, inputsToCheck: Int): Long {
		for (i in inputsToCheck until input.size) {
			if (!isValid(input, i, inputsToCheck))
				return input[i]
		}
		throw IllegalArgumentException("All data is valid")
	}

	override fun part1(input: List<Long>): Long {
		return part1(input, 25)
	}

	override fun part2(input: List<Long>) {
	}

	class Tests {
		private val task = Day9()

		private val isValidRawInput = (listOf(20) + ((1..25) - 20)).joinToString("\n")

		private val rawInput = """
			35
			20
			15
			25
			47
			40
			62
			55
			65
			95
			102
			117
			150
			182
			127
			219
			299
			277
			309
			576
		""".trimIndent()

		@TestFactory
		fun isValidMax25(): Collection<DynamicTest> = createTestCases(
			"${isValidRawInput}\n26" expects true,
			"${isValidRawInput}\n49" expects true,
			"${isValidRawInput}\n100" expects false,
			"${isValidRawInput}\n50" expects false,
			"${isValidRawInput}\n45\n26" expects true,
			"${isValidRawInput}\n45\n65" expects false,
			"${isValidRawInput}\n45\n64" expects true,
			"${isValidRawInput}\n45\n66" expects true
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.isValid(input, input.size - 1, 25))
		}

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(127, task.part1(input, 5))
		}
	}
}