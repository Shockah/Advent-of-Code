package pl.shockah.aoc.y2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.expects
import java.util.*

class Day9: Intcode.AdventTask<Long, Long>(2019, 9, instructions) {
	companion object {
		val adjustBase = Intcode.Instruction(9) { pointer, relativeBase, parameters, memory, console ->
			val value = parameters.read(pointer, relativeBase, memory).toInt()
			relativeBase.value += value
		}

		val instructions = Day7.instructions + listOf(adjustBase)
	}

	override fun part1(input: List<Long>): Long {
		val inputBuffer = LinkedList<Long>()
		val outputBuffer = LinkedList<Long>()
		inputBuffer.add(1)

		val intcode = getIntcode(input, inputBuffer, outputBuffer)
		intcode.execute()
		if (outputBuffer.size != 1)
			throw IllegalStateException("Output buffer should contain only 1 value.")
		return outputBuffer.removeFirst()
	}

	override fun part2(input: List<Long>): Long {
		TODO("not implemented")
	}

	@Nested
	inner class Tests {
		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				listOf(109L, 1L, 204L, -1L, 1001L, 100L, 1L, 100L, 1008L, 100L, 16L, 101L, 1006L, 101L, 0L, 99L) expects listOf(109L, 1L, 204L, -1L, 1001L, 100L, 1L, 100L, 1008L, 100L, 16L, 101L, 1006L, 101L, 0L, 99L),
				listOf(104L ,1125899906842624L, 99L) expects listOf(1125899906842624L)
		) { input, expected ->
			val output = LinkedList<Long>()
			val intcode = getIntcode(input, null, output)
			intcode.execute()
			Assertions.assertEquals(expected, output.toList())
		}
	}
}