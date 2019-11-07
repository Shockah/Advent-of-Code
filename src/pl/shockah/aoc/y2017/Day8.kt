package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import java.util.regex.Pattern
import kotlin.math.max

class Day8: AdventTask<List<Day8.Instruction>, Int, Int>(2017, 8) {
	private val inputPattern: Pattern = Pattern.compile("(\\w+) ((?:inc)|(?:dec)) (-?\\d+) if (\\w+) ((?:==)|(?:!=)|(?:>)|(?:<)|(?:>=)|(?:<=)) (-?\\d+)")

	enum class Operator(
			val symbol: String,
			private val lambda: (Int, Int) -> Boolean
	) {
		Equals("==", { a, b -> a == b }),
		NotEquals("!=", { a, b -> a != b }),
		Greater(">", { a, b -> a > b }),
		Lower("<", { a, b -> a < b }),
		GreaterOrEqual(">=", { a, b -> a >= b }),
		LowerOrEqual("<=", { a, b -> a <= b });

		operator fun invoke(a: Int, b: Int): Boolean {
			return lambda(a, b)
		}

		companion object {
			val bySymbol = values().map { it.symbol to it }.toMap()
		}
	}

	data class Instruction(
			val register: String,
			val adding: Int,
			val conditionRegister: String,
			val conditionOperator: Operator,
			val conditionValue: Int
	)

	override fun parseInput(rawInput: String): List<Instruction> {
		return rawInput.lines().map {
			val matcher = inputPattern.matcher(it)
			require(matcher.find())

			val register = matcher.group(1)
			val adding = matcher.group(3).toInt() * (if (matcher.group(2) == "dec") -1 else 1)
			val conditionRegister = matcher.group(4)
			val conditionOperator = Operator.bySymbol[matcher.group(5)]!!
			val conditionValue = matcher.group(6).toInt()
			return@map Instruction(register, adding, conditionRegister, conditionOperator, conditionValue)
		}
	}

	private enum class Mode {
		FinalMaxValue, MaxValueEver
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun task(input: List<Instruction>, mode: Mode): Int {
		val registers = mutableMapOf<String, Int>()
		var maxValue = 0
		for (instruction in input) {
			if (instruction.conditionOperator(registers[instruction.conditionRegister] ?: 0, instruction.conditionValue)) {
				registers[instruction.register] = (registers[instruction.register] ?: 0) + instruction.adding
				if (mode == Mode.MaxValueEver)
					maxValue = max(maxValue, registers[instruction.register]!!)
			}
		}

		return when (mode) {
			Mode.FinalMaxValue -> registers.values.max()!!
			Mode.MaxValueEver -> maxValue
		}
	}

	override fun part1(input: List<Instruction>): Int {
		return task(input, Mode.FinalMaxValue)
	}

	override fun part2(input: List<Instruction>): Int {
		return task(input, Mode.MaxValueEver)
	}

	class Tests {
		private val task = Day8()

		private val rawInput = """
			b inc 5 if a > 1
			a inc 1 if b < 5
			c dec -10 if a >= 1
			c inc -20 if c == 10
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(1, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(10, task.part2(input))
		}
	}
}