package pl.shockah.aoc.y2017

import java.util.regex.Pattern

class Day2(
		useExampleInputFile: Boolean = false
): Year2017<List<List<Int>>, Int, Int>(useExampleInputFile) {
	private val inputSplitPattern: Pattern = Pattern.compile("\\s+")

	override val parsedInput: List<List<Int>> by lazy {
		inputFile.readLines().map {
			it.trim().split(inputSplitPattern).map { it.toInt() }
		}
	}

	override fun part1(): Int {
		return parsedInput.map { it.max()!! - it.min()!! }.sum()
	}

	override fun part2(): Int {
		return parsedInput.map { line ->
			val sorted = line.sorted()
			sorted.mapIndexed { index: Int, value: Int ->
				var divided = 0
				for (i in 0 until index) {
					val value2 = sorted[i]
					if (value2 % value == 0) {
						if (divided != 0)
							continue
						divided = value2 / value
					}
				}
				if (divided != 0)
					return@map divided
			}
			throw IllegalArgumentException("Invalid input data.")
		}.sum()
	}
}