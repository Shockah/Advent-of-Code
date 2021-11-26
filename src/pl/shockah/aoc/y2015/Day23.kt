package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse2
import pl.shockah.util.Box
import java.math.BigInteger
import java.util.regex.Pattern

class Day23: AdventTask<List<Day23.Instruction>, BigInteger, BigInteger>(2015, 23) {
	private val inputPattern: Pattern = Pattern.compile("(.+?) (.+)")

	@Suppress("EnumEntryName")
	enum class Register {
		a, b
	}

	sealed class Instruction {
		abstract fun execute(registers: Array<BigInteger>, counter: Box<Int>)

		data class Half(
			val register: Register
		): Instruction() {
			override fun execute(registers: Array<BigInteger>, counter: Box<Int>) {
				registers[register.ordinal] /= 2.toBigInteger()
				counter.value++
			}
		}

		data class Triple(
			val register: Register
		): Instruction() {
			override fun execute(registers: Array<BigInteger>, counter: Box<Int>) {
				registers[register.ordinal] *= 3.toBigInteger()
				counter.value++
			}
		}

		data class Increment(
			val register: Register
		): Instruction() {
			override fun execute(registers: Array<BigInteger>, counter: Box<Int>) {
				registers[register.ordinal]++
				counter.value++
			}
		}

		data class Jump(
			val offset: Int
		): Instruction() {
			override fun execute(registers: Array<BigInteger>, counter: Box<Int>) {
				counter.value += offset
			}
		}

		data class JumpIfEven(
			val register: Register,
			val offset: Int
		): Instruction() {
			override fun execute(registers: Array<BigInteger>, counter: Box<Int>) {
				if (registers[register.ordinal] % 2.toBigInteger() == BigInteger.ZERO)
					counter.value += offset
				else
					counter.value++
			}
		}

		data class JumpIfOne(
			val register: Register,
			val offset: Int
		): Instruction() {
			override fun execute(registers: Array<BigInteger>, counter: Box<Int>) {
				if (registers[register.ordinal] == BigInteger.ONE)
					counter.value += offset
				else
					counter.value++
			}
		}
	}

	override fun parseInput(rawInput: String): List<Instruction> {
		fun parseRegister(text: String): Register {
			return Register.valueOf(text.lowercase())
		}

		fun parseOffset(text: String): Int {
			return Integer.parseInt(text)
		}

		return rawInput.lines().map {
			val (instruction, argumentsString) = inputPattern.parse2<String, String>(it)
			val arguments = argumentsString.split(",").map { it.trim() }
			when (instruction.lowercase()) {
				"hlf" -> Instruction.Half(parseRegister(arguments[0]))
				"tpl" -> Instruction.Triple(parseRegister(arguments[0]))
				"inc" -> Instruction.Increment(parseRegister(arguments[0]))
				"jmp" -> Instruction.Jump(parseOffset(arguments[0]))
				"jie" -> Instruction.JumpIfEven(parseRegister(arguments[0]), parseOffset(arguments[1]))
				"jio" -> Instruction.JumpIfOne(parseRegister(arguments[0]), parseOffset(arguments[1]))
				else -> throw IllegalArgumentException()
			}
		}
	}

	private fun task(instructions: List<Instruction>, registers: Array<BigInteger>): BigInteger {
		require(registers.size == Register.values().size)
		val counter = Box(0)

		while (counter.value < instructions.size) {
			val instruction = instructions[counter.value]
			instruction.execute(registers, counter)
		}
		return registers[Register.b.ordinal]
	}

	override fun part1(input: List<Instruction>): BigInteger {
		return task(input, Array(Register.values().size) { BigInteger.ZERO })
	}

	override fun part2(input: List<Instruction>): BigInteger {
		return task(input, Array(Register.values().size) {
			when (it) {
				Register.a.ordinal -> BigInteger.ONE
				else -> BigInteger.ZERO
			}
		})
	}

	class Tests {
		private val task = Day23()

		private val rawInput = """
			inc b
			jio b, +2
			tpl b
			inc b
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(2.toBigInteger(), task.part1(input))
		}
	}
}