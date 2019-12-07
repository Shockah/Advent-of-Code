package pl.shockah.aoc.y2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import java.util.*

class Day2: AdventTask<List<Int>, Int, Int>(2019, 2), Intcode.Provider {
	companion object {
		val add = Intcode.Instruction(1) { pointer, parameters, memory, _ ->
			val a = parameters.read(pointer, memory)
			val b = parameters.read(pointer, memory)
			val output = memory[pointer.value++]
			memory[output] = a + b
		}

		val multiply = Intcode.Instruction(2) { pointer, parameters, memory, _ ->
			val a = parameters.read(pointer, memory)
			val b = parameters.read(pointer, memory)
			val output = memory[pointer.value++]
			memory[output] = a * b
		}

		val halt = Intcode.Instruction(99) { _, _, _, console ->
			console.halt()
		}

		val instructions = listOf(add, multiply, halt)
	}

	override fun getIntcode(initialMemory: List<Int>, input: LinkedList<Int>?, output: LinkedList<Int>?): Intcode {
		return Intcode(initialMemory, input, output).apply {
			register(instructions)
		}
	}

	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.split(",").map { it.toInt() }
	}

	private fun task(input: List<Int>, noun: Int, verb: Int): Int {
		val data = input.toMutableList()
		data[1] = noun
		data[2] = verb

		val intcode = getIntcode(data)
		intcode.execute()
		return intcode.memory[0]
	}

	override fun part1(input: List<Int>): Int {
		return task(input, 12, 2)
	}

	override fun part2(input: List<Int>): Int {
		for (noun in 0..99) {
			for (verb in 0..99) {
				if (task(input, noun, verb) == 19690720) {
					println("Found combination: noun = $noun, verb = $verb")
					return 100 * noun + verb
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
		) { input, expected -> Assertions.assertEquals(expected, getIntcode(input).also { it.execute() }.memory) }
	}
}