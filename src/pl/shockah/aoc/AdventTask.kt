package pl.shockah.aoc

import java.io.File
import java.util.regex.Pattern

abstract class AdventTask<ParsedInput, A, B>(
		val year: Int,
		val day: Int
) {
	protected val zeroAscii: Int = '0'.toInt()
	protected val inputSplitPattern: Pattern = Pattern.compile("\\s+")

	abstract fun parseInput(file: File): ParsedInput

	abstract fun part1(input: ParsedInput): A

	abstract fun part2(input: ParsedInput): B
}