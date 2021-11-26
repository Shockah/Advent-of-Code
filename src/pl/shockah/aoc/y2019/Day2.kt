package pl.shockah.aoc.y2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.expects

class Day2: Intcode.AdventTask<Long, Long>(2019, 2, instructions) {
	companion object {
		val add = Intcode.Instruction(1) { pointer, relativeBase, parameters, memory, _ ->
			val a = parameters.read(pointer, relativeBase, memory)
			val b = parameters.read(pointer, relativeBase, memory)
			val output = parameters.getAddress(pointer, relativeBase, memory)
			memory[output] = a + b
		}

		val multiply = Intcode.Instruction(2) { pointer, relativeBase, parameters, memory, _ ->
			val a = parameters.read(pointer, relativeBase, memory)
			val b = parameters.read(pointer, relativeBase, memory)
			val output = parameters.getAddress(pointer, relativeBase, memory)
			memory[output] = a * b
		}

		val halt = Intcode.Instruction(99) { _, _, _, _, console ->
			console.halt()
		}

		val instructions = listOf(add, multiply, halt)
	}

	private fun task(input: List<Long>, noun: Long, verb: Long): Long {
		val data = input.toMutableList()
		data[1] = noun
		data[2] = verb

		val intcode = getIntcode(data)
		intcode.execute()
		return intcode.memory[0]
	}

	override fun part1(input: List<Long>): Long {
		return task(input, 12, 2)
	}

	override fun part2(input: List<Long>): Long {
		for (noun in 0L..99L) {
			for (verb in 0L..99L) {
				if (task(input, noun, verb) == 19690720L) {
					println("Found combination: noun = $noun, verb = $verb")
					return 100L * noun + verb
				}
			}
		}
		throw IllegalStateException("No noun+verb combination results in 19690720.")
	}

	@Nested
	inner class Tests {
		@TestFactory
		fun execute(): Collection<DynamicTest> = createTestCases(
			listOf(1, 0, 0, 0, 99) expects listOf(2, 0, 0, 0, 99),
			listOf(2, 3, 0, 3, 99) expects listOf(2, 3, 0, 6, 99),
			listOf(2, 4, 4, 5, 99, 0) expects listOf(2, 4, 4, 5, 99, 9801),
			listOf(1, 1, 1, 4, 99, 5, 6, 0, 99) expects listOf(30, 1, 1, 4, 2, 5, 6, 0, 99)
		) { input, expected -> Assertions.assertEquals(expected.map { it.toLong() }, getIntcode(input.map { it.toLong() }).also { it.execute() }.memory.data.values.toList()) }
	}
}