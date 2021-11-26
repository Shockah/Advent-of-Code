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

fun <ParsedInput, A, B, Self: AdventTask<ParsedInput, A, B>, Output> Self.createRawTestCases(vararg cases: AdventTask.Case<String, Output>, executable: (Self, input: ParsedInput) -> Output): Collection<DynamicTest> {
	return createRawTestCases(cases.toList(), executable)
}

fun <ParsedInput, A, B, Self: AdventTask<ParsedInput, A, B>, Output> Self.createRawTestCases(cases: List<AdventTask.Case<String, Output>>, executable: (Self, input: ParsedInput) -> Output): Collection<DynamicTest> {
	return AdventTask.createTestCases(cases.toList()) { input, expected ->
		Assertions.assertEquals(
			expected,
			executable(this, parseInput(input))
		)
	}
}

fun <ParsedInput, A, B, Self: AdventTask<ParsedInput, A, B>> Self.createRawPart1TestCases(vararg cases: AdventTask.Case<String, A>): Collection<DynamicTest> {
	return createRawPart1TestCases(cases.toList())
}

fun <ParsedInput, A, B, Self: AdventTask<ParsedInput, A, B>> Self.createRawPart1TestCases(cases: List<AdventTask.Case<String, A>>): Collection<DynamicTest> {
	return createRawTestCases(cases) { task, input -> task.part1(input) }
}

fun <ParsedInput, A, B, Self: AdventTask<ParsedInput, A, B>> Self.createRawPart2TestCases(vararg cases: AdventTask.Case<String, B>): Collection<DynamicTest> {
	return createRawPart2TestCases(cases.toList())
}

fun <ParsedInput, A, B, Self: AdventTask<ParsedInput, A, B>> Self.createRawPart2TestCases(cases: List<AdventTask.Case<String, B>>): Collection<DynamicTest> {
	return createRawTestCases(cases) { task, input -> task.part2(input) }
}