package pl.shockah.aoc

import java.io.File

abstract class AdventTask<ParsedInput, A, B> {
	open val inputFile: File
		get() = File("input/${this::class.simpleName}.txt")

	abstract val parsedInput: ParsedInput

	abstract fun part1(): A

	abstract fun part2(): B
}