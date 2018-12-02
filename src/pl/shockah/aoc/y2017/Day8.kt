package pl.shockah.aoc.y2017

import pl.shockah.aoc.AdventTask
import java.io.File
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
	}

	data class Instruction(
			val register: String,
			val adding: Int,
			val conditionRegister: String,
			val conditionOperator: Operator,
			val conditionValue: Int
	)

	override fun parseInput(file: File): List<Instruction> {
		return file.readLines().map {
			val matcher = inputPattern.matcher(it)
			if (!matcher.find())
				throw IllegalArgumentException()

			val register = matcher.group(1)
			val adding = matcher.group(3).toInt() * (if (matcher.group(2) == "dec") -1 else 1)
			val conditionRegister = matcher.group(4)
			val conditionOperator = Operator.values().first { it.symbol == matcher.group(5) }
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
			if (instruction.conditionOperator(registers.computeIfAbsent(instruction.conditionRegister) { 0 }, instruction.conditionValue)) {
				registers[instruction.register] = registers.computeIfAbsent(instruction.register) { 0 } + instruction.adding
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
}