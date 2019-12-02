package pl.shockah.aoc.y2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

class Day2: AdventTask<List<Int>, Int, Int>(2019, 2) {
	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.split(",").map { it.toInt() }
	}

	fun execute(input: List<Int>): List<Int> {
		val data = input.toMutableList()
		var index = 0
		loop@ while (true) {
			when (val opcode = data[index++]) {
				99 -> break@loop
				1 -> {
					val a = data[index++]
					val b = data[index++]
					val output = data[index++]
					data[output] = data[a] + data[b]
				}
				2 -> {
					val a = data[index++]
					val b = data[index++]
					val output = data[index++]
					data[output] = data[a] * data[b]
				}
				else -> throw IllegalStateException("Invalid opcode $opcode")
			}
		}
		return data
	}

	private fun task(input: List<Int>, noun: Int, verb: Int): Int {
		val data = input.toMutableList()
		data[1] = noun
		data[2] = verb
		return execute(data)[0]
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

	class Tests {
		private val task = Day2()

		@TestFactory
		fun execute(): Collection<DynamicTest> = createTestCases(
				listOf(1, 0, 0, 0, 99) expects listOf(2, 0, 0, 0, 99),
				listOf(2, 3, 0, 3, 99) expects listOf(2, 3, 0, 6, 99),
				listOf(2, 4, 4, 5, 99, 0) expects listOf(2, 4, 4, 5, 99, 9801),
				listOf(1, 1, 1, 4, 99, 5, 6, 0, 99) expects listOf(30, 1, 1, 4, 2, 5, 6, 0, 99)
		) { input, expected -> Assertions.assertEquals(expected, task.execute(input)) }
	}
}