package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.util.Box

class Day23 : AdventTask<List<Day23.Instruction>, Int, Int>(2015, 23) {
	enum class Register {
		a, b
	}

	sealed class Instruction {
		abstract fun execute(registers: IntArray, counter: Box<Int>)

		data class Half(
				val register: Register
		) : Instruction() {
			override fun execute(registers: IntArray, counter: Box<Int>) {
				registers[register.ordinal] /= 2
				counter.value++
			}
		}

		data class Triple(
				val register: Register
		) : Instruction() {
			override fun execute(registers: IntArray, counter: Box<Int>) {
				registers[register.ordinal] *= 3
				counter.value++
			}
		}

		data class Increment(
				val register: Register
		) : Instruction() {
			override fun execute(registers: IntArray, counter: Box<Int>) {
				registers[register.ordinal]++
				counter.value++
			}
		}

		data class Jump(
				val offset: Int
		) : Instruction() {
			override fun execute(registers: IntArray, counter: Box<Int>) {
				counter.value += offset
			}
		}

		data class JumpIfEven(
				val register: Register,
				val offset: Int
		) : Instruction() {
			override fun execute(registers: IntArray, counter: Box<Int>) {
				if (registers[register.ordinal] % 2 == 0)
					counter.value += offset
				else
					counter.value++
			}
		}

		data class JumpIfOne(
				val register: Register,
				val offset: Int
		) : Instruction() {
			override fun execute(registers: IntArray, counter: Box<Int>) {
				if (registers[register.ordinal] == 1)
					counter.value += offset
				else
					counter.value++
			}
		}
	}

	override fun parseInput(rawInput: String): List<Instruction> {
		return rawInput.lines().map {
			TODO()
		}
	}

	override fun part1(input: List<Instruction>): Int {
		TODO()
	}

	override fun part2(input: List<Instruction>): Int {
		TODO()
	}

	class Tests {
		private val task = Day14()

		private val rawInput = """
            inc b
			jio b, +2
			tpl b
			inc b
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(2, task.part1(input))
		}
	}
}