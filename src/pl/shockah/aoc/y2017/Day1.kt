package pl.shockah.aoc.y2017

import kotlin.streams.toList

class Day1: Year2017<List<Int>, Int, Int>() {
	private val zeroAscii: Int = '0'.toInt()

	override val parsedInput: List<Int> by lazy {
		inputFile.readText().trim().chars().map { it - zeroAscii }.toList()
	}

	private enum class Mode {
		Next, HalfwayThrough
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun task(mode: Mode): Int {
		var sum = 0
		for (i in 0 until parsedInput.size) {
			val indexToCheck = when (mode) {
				Mode.Next -> i + 1
				Mode.HalfwayThrough -> i + parsedInput.size / 2
			}
			if (parsedInput[i] == parsedInput[indexToCheck % parsedInput.size])
				sum += parsedInput[i]
		}
		return sum
	}

	override fun part1(): Int {
		return task(Mode.Next)
	}

	override fun part2(): Int {
		return task(Mode.HalfwayThrough)
	}
}