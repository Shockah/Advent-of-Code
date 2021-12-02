package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse
import java.util.regex.Pattern

class Day25: AdventTask<Day25.Input, Int, Unit>(2017, 25) {
	private val beginPattern = Pattern.compile("Begin in state (\\w)\\.")
	private val checksumPattern = Pattern.compile("Perform a diagnostic checksum after (\\d+) steps\\.")
	private val statePattern = Pattern.compile("In state (\\w):")
	private val conditionPattern = Pattern.compile("\\s*If the current value is (\\d+):")
	private val conditionWritePattern = Pattern.compile("\\s*- Write the value (\\d+)\\.")
	private val conditionMovePattern = Pattern.compile("\\s*- Move one slot to the (left|right)\\.")
	private val conditionContinuePattern = Pattern.compile("\\s*- Continue with state (\\w)\\.")

	data class Input(
		val initialState: Char,
		val steps: Int,
		val states: Map<Char, List<Condition>>
	) {
		data class Condition(
			val conditionValue: Int,
			val writeValue: Int,
			val moveDirection: Int,
			val newState: Char
		)
	}

	override fun parseInput(rawInput: String): Input {
		val lines = rawInput.trim().lines()
		var index = 0
		val initialState = beginPattern.parse<Char>(lines[index++])
		val steps = checksumPattern.parse<Int>(lines[index++])

		val states = mutableMapOf<Char, List<Input.Condition>>()
		while (index < lines.size) {
			if (lines[index].isEmpty()) {
				index++
				continue
			}

			val state = statePattern.parse<Char>(lines[index++])
			val conditions = mutableListOf<Input.Condition>()
			while (index < lines.size && lines[index].isNotEmpty()) {
				val conditionValue = conditionPattern.parse<Int>(lines[index++])
				val writeValue = conditionWritePattern.parse<Int>(lines[index++])
				val moveDirection = if (conditionMovePattern.parse<String>(lines[index++]) == "right") 1 else -1
				val newState = conditionContinuePattern.parse<Char>(lines[index++])
				conditions += Input.Condition(conditionValue, writeValue, moveDirection, newState)
			}
			states[state] = conditions
		}

		return Input(initialState, steps, states)
	}

	override fun part1(input: Input): Int {
		var state = input.initialState
		var index = 0
		val tape = mutableMapOf<Int, Int>()
		repeat(input.steps) {
			val conditions = input.states[state]!!
			for (condition in conditions) {
				if ((tape[index] ?: 0) == condition.conditionValue) {
					tape[index] = condition.writeValue
					index += condition.moveDirection
					state = condition.newState
					break
				}
			}
		}
		return tape.values.count { it == 1 }
	}

	override fun part2(input: Input) {
	}

	class Tests {
		private val task = Day25()

		private val rawInput = """
			Begin in state A.
			Perform a diagnostic checksum after 6 steps.
			
			In state A:
			  If the current value is 0:
			    - Write the value 1.
			    - Move one slot to the right.
			    - Continue with state B.
			  If the current value is 1:
			    - Write the value 0.
			    - Move one slot to the left.
			    - Continue with state B.
			
			In state B:
			  If the current value is 0:
			    - Write the value 1.
			    - Move one slot to the left.
			    - Continue with state A.
			  If the current value is 1:
			    - Write the value 1.
			    - Move one slot to the right.
			    - Continue with state A.
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(3, task.part1(input))
		}
	}
}