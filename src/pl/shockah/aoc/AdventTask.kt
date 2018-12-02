package pl.shockah.aoc

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
}