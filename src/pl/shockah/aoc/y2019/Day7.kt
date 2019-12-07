package pl.shockah.aoc.y2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.expects
import java.util.*

class Day7: Intcode.AdventTask<Int, Int>(2019, 7, instructions) {
	companion object {
		val instructions = Day5.instructions
	}

	private fun getCombinations(): Set<List<Int>> {
		return getCombinations(setOf(0, 1, 2, 3, 4), emptyList())
	}

	private fun getCombinations(left: Set<Int>, current: List<Int>): Set<List<Int>> {
		if (left.isEmpty())
			return setOf(current)
		return left.flatMap { getCombinations(left - it, current + it) }.toSet()
	}

	override fun part1(input: List<Int>): Int {
		fun getOutput(combination: List<Int>): Int {
			val inputBuffer = LinkedList<Int>()
			val outputBuffer = LinkedList<Int>()

			outputBuffer.add(0)
			for (phase in combination) {
				inputBuffer.add(phase)
				inputBuffer.add(outputBuffer.removeFirst())
				val intcode = getIntcode(input, inputBuffer, outputBuffer)
				intcode.execute()
			}
			return outputBuffer.removeFirst()
		}

		val combinations = getCombinations()
		return combinations.map { getOutput(it) }.max()!!
	}

	override fun part2(input: List<Int>): Int {
		TODO()
	}

	@Nested
	inner class Tests {
		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				listOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0) expects 43210,
				listOf(3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23, 101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0) expects 54321,
				listOf(3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33, 1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0) expects 65210
		) { input, expected -> Assertions.assertEquals(expected, part1(input)) }
	}
}