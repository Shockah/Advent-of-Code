package pl.shockah.aoc.y2018

import pl.shockah.aoc.AdventTask
import java.io.File

abstract class Year2018<ParsedInput, A, B> : AdventTask<ParsedInput, A, B>() {
	override val inputFile: File
		get() = File("input/2018/${this::class.simpleName}.txt")
}