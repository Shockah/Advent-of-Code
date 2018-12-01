package pl.shockah.aoc.y2017

import pl.shockah.aoc.AdventTask
import java.io.File

abstract class Year2017<ParsedInput, A, B> : AdventTask<ParsedInput, A, B>() {
	override val inputFile: File
		get() = File("input/2017/${this::class.simpleName}.txt")
}