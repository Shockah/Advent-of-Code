package pl.shockah.aoc.y2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.expects
import java.util.*

class Day7: Intcode.AdventTask<Long, Long>(2019, 7, instructions) {
	companion object {
		val instructions = Day5.instructions
	}

	private fun getCombinations(range: LongRange): Set<List<Long>> {
		return getCombinations(range.toSet(), emptyList())
	}

	private fun getCombinations(left: Set<Long>, current: List<Long>): Set<List<Long>> {
		if (left.isEmpty())
			return setOf(current)
		return left.flatMap { getCombinations(left - it, current + it) }.toSet()
	}

	override fun part1(input: List<Long>): Long {
		fun getOutput(combination: List<Long>): Long {
			val inputBuffer = LinkedList<Long>()
			val outputBuffer = LinkedList<Long>()

			outputBuffer.add(0)
			for (phase in combination) {
				inputBuffer.add(phase)
				inputBuffer.add(outputBuffer.removeFirst())
				val intcode = getIntcode(input, inputBuffer, outputBuffer)
				intcode.execute()
			}
			return outputBuffer.removeFirst()
		}

		val combinations = getCombinations(0L..4L)
		return combinations.maxOf { getOutput(it) }
	}

	override fun part2(input: List<Long>): Long {
		fun getOutput(combination: List<Long>): Long {
			val buffers = combination.map { LinkedList<Long>().apply { add(it) } }
			val intcodes = combination.mapIndexed { index, _ -> getIntcode(input, buffers[index], buffers[(index + 1) % combination.size]) }
			buffers[0].add(0)

			var iterations = 0
			while (true) {
				val intcode = intcodes.firstOrNull { !it.halted && !it.awaiting } ?: break
				intcode.execute()
				iterations++
			}
			println("Combination $combination, iterations $iterations, result ${buffers[0][0]}")
			return buffers[0][0]
		}

		val combinations = getCombinations(5L..9L)
		return combinations.maxOf { getOutput(it) }
	}

	@Nested
	inner class Tests {
		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
			listOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0) expects 43210L,
			listOf(3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23, 101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0) expects 54321L,
			listOf(3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33, 1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0) expects 65210L
		) { input, expected -> Assertions.assertEquals(expected, part1(input.map { it.toLong() })) }

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
			listOf(3, 26, 1001, 26, -4, 26, 3, 27, 1002, 27, 2, 27, 1, 27, 26, 27, 4, 27, 1001, 28, -1, 28, 1005, 28, 6, 99, 0, 0, 5) expects 139629729L,
			listOf(3, 52, 1001, 52, -5, 52, 3, 53, 1, 52, 56, 54, 1007, 54, 5, 55, 1005, 55, 26, 1001, 54, -5, 54, 1105, 1, 12, 1, 53, 54, 53, 1008, 54, 0, 55, 1001, 55, 1, 55, 2, 53, 55, 53, 4, 53, 1001, 56, -1, 56, 1005, 56, 6, 99, 0, 0, 0, 0, 10) expects 18216L
		) { input, expected -> Assertions.assertEquals(expected, part2(input.map { it.toLong() })) }
	}
}