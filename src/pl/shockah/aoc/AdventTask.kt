package pl.shockah.aoc

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import java.util.regex.Pattern

abstract class AdventTask<ParsedInput, A, B>(
		val year: Int,
		val day: Int
) {
	protected val zeroAscii: Int = '0'.code
	protected val inputSplitPattern: Pattern = Pattern.compile("\\s+")

	abstract fun parseInput(rawInput: String): ParsedInput

	abstract fun part1(input: ParsedInput): A

	abstract fun part2(input: ParsedInput): B

	data class Case<Input, Output>(
			val input: Input,
			val expected: Output
	)

	companion object {
		fun <Input, Output> createSimpleTestCases(vararg cases: Case<Input, Output>, executable: (input: Input) -> Output): Collection<DynamicTest> {
			return createSimpleTestCases(cases.toList(), executable)
		}

		fun <Input, Output> createTestCases(vararg cases: Case<Input, Output>, executable: (input: Input, expected: Output) -> Unit): Collection<DynamicTest> {
			return createTestCases(cases.toList(), executable)
		}

		fun <Input, Output> createSimpleTestCases(cases: List<Case<Input, Output>>, executable: (input: Input) -> Output): Collection<DynamicTest> {
			return createTestCases(cases) { input, expected -> Assertions.assertEquals(expected, executable(input)) }
		}

		fun <Input, Output> createTestCases(cases: List<Case<Input, Output>>, executable: (input: Input, expected: Output) -> Unit): Collection<DynamicTest> {
			return cases.mapIndexed { index: Int, case: Case<Input, Output> ->
				DynamicTest.dynamicTest("#${index + 1} - input: ${case.input}") {
					executable(case.input, case.expected)
				}
			}
		}
	}
}

infix fun <Input, Output> Input.expects(expected: Output): AdventTask.Case<Input, Output> {
	return AdventTask.Case(this, expected)
}