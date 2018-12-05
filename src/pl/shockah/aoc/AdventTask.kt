package pl.shockah.aoc

import org.junit.jupiter.api.DynamicTest
import java.util.regex.Pattern

abstract class AdventTask<ParsedInput, A, B>(
		val year: Int,
		val day: Int
) {
	protected val zeroAscii: Int = '0'.toInt()
	protected val inputSplitPattern: Pattern = Pattern.compile("\\s+")

	abstract fun parseInput(rawInput: String): ParsedInput

	abstract fun part1(input: ParsedInput): A

	abstract fun part2(input: ParsedInput): B

	data class Case<R>(
			val rawInput: String,
			val expected: R
	)

	companion object {
		fun <R> createTestCases(vararg cases: Case<R>, executable: (rawInput: String, expected: R) -> Unit): Collection<DynamicTest> {
			return createTestCases(cases.toList(), executable)
		}

		fun <R> createTestCases(cases: List<Case<R>>, executable: (rawInput: String, expected: R) -> Unit): Collection<DynamicTest> {
			return cases.mapIndexed { index: Int, case: Case<R> ->
				DynamicTest.dynamicTest("#${index + 1} - input: ${case.rawInput.lines().joinToString("; ")}") {
					executable(case.rawInput, case.expected)
				}
			}
		}
	}
}

infix fun <R> String.expects(expected: R): AdventTask.Case<R> {
	return AdventTask.Case(this, expected)
}