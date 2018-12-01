package pl.shockah.aoc.y2017

import pl.shockah.aoc.AdventTask
import java.io.File

abstract class Year2017<ParsedInput, A, B>(
		useExampleInputFile: Boolean = false
) : AdventTask<ParsedInput, A, B>(useExampleInputFile) {
	override val actualInputFile: File
		get() = File("input/2017/${this::class.simpleName}.txt")

	override val exampleInputFile: File
		get() = File("input/2017/${this::class.simpleName}-example.txt")
}