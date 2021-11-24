package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse2
import pl.shockah.util.Box
import java.util.regex.Pattern

class Day8: AdventTask<List<Day8.Instruction>, Int, Int>(2020, 8) {
	private val inputPattern = Pattern.compile("(\\w+) ([+\\-]\\d+)")

	sealed class Instruction {
		abstract fun execute(accumulator: Box<Int>, counter: Box<Int>)

		data class NoOperation(
			val offset: Int
		): Instruction() {
			override fun execute(accumulator: Box<Int>, counter: Box<Int>) {
				counter.value += 1
			}
		}

		data class Accumulate(
			val value: Int
		): Instruction() {
			override fun execute(accumulator: Box<Int>, counter: Box<Int>) {
				accumulator.value += value
				counter.value++
			}
		}

		data class Jump(
			val offset: Int
		): Instruction() {
			override fun execute(accumulator: Box<Int>, counter: Box<Int>) {
				counter.value += offset
			}
		}
	}

	override fun parseInput(rawInput: String): List<Instruction> {
		return rawInput.lines().map {
			val (instruction, argument) = inputPattern.parse2<String, Int>(it)
			when (instruction.lowercase()) {
				"nop" -> Instruction.NoOperation(argument)
				"acc" -> Instruction.Accumulate(argument)
				"jmp" -> Instruction.Jump(argument)
				else -> throw IllegalArgumentException()
			}
		}
	}

	private fun execute(input: List<Instruction>, accumulator: Box<Int>, counter: Box<Int>) {
		val executedInstructionIndexes = mutableSetOf<Int>()

		while (counter.value < input.size) {
			val instructionIndex = counter.value
			if (executedInstructionIndexes.contains(instructionIndex))
				return
			val instruction = input[instructionIndex]
			instruction.execute(accumulator, counter)
			executedInstructionIndexes.add(instructionIndex)
		}
	}

	override fun part1(input: List<Instruction>): Int {
		val accumulator = Box(0)
		val counter = Box(0)
		execute(input, accumulator, counter)
		return accumulator.value
	}

	override fun part2(input: List<Instruction>): Int {
		input.indices.filter { input[it] is Instruction.NoOperation || input[it] is Instruction.Jump }.forEach { repairIndex ->
			val newInput = input.mapIndexed { index, instruction ->
				when (instruction) {
					is Instruction.NoOperation -> return@mapIndexed if (repairIndex == index) Instruction.Jump(instruction.offset) else Instruction.NoOperation(instruction.offset)
					is Instruction.Jump -> return@mapIndexed if (repairIndex == index) Instruction.NoOperation(instruction.offset) else Instruction.Jump(instruction.offset)
					else -> return@mapIndexed instruction
				}
			}
			val accumulator = Box(0)
			val counter = Box(0)
			execute(newInput, accumulator, counter)
			if (counter.value >= newInput.size)
				return accumulator.value
		}
		throw IllegalArgumentException("Could not repair program")
	}

	class Tests {
		private val task = Day8()

		private val rawInput = """
			nop +0
			acc +1
			jmp +4
			acc +3
			jmp -3
			acc -99
			acc +1
			jmp -4
			acc +6
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(5, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(8, task.part2(input))
		}
	}
}