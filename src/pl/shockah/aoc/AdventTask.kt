package pl.shockah.aoc

import java.io.File

abstract class AdventTask<ParsedInput, A, B>(
		private val useExampleInputFile: Boolean = false
) {
	protected val zeroAscii: Int = '0'.toInt()

	val inputFile: File
		get() = if (useExampleInputFile) exampleInputFile else actualInputFile

	open val actualInputFile: File
		get() = File("input/${this::class.simpleName}.txt")

	open val exampleInputFile: File
		get() = File("input/${this::class.simpleName}-example.txt")

	abstract val parsedInput: ParsedInput

	abstract fun part1(): A

	abstract fun part2(): B
}