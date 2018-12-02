package pl.shockah.aoc.y2017

import pl.shockah.aoc.AdventTask
import kotlin.streams.toList

class Day1 : AdventTask<List<Int>, Int, Int>(2017, 1) {
	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.chars().map { it - zeroAscii }.toList()
	}

	private enum class Mode {
		Next, HalfwayThrough
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun task(input: List<Int>, mode: Mode): Int {
		var sum = 0
		for (i in 0 until input.size) {
			val indexToCheck = when (mode) {
				Mode.Next -> i + 1
				Mode.HalfwayThrough -> i + input.size / 2
			}
			if (input[i] == input[indexToCheck % input.size])
				sum += input[i]
		}
		return sum
	}

	override fun part1(input: List<Int>): Int {
		return task(input, Mode.Next)
	}

	override fun part2(input: List<Int>): Int {
		return task(input, Mode.HalfwayThrough)
	}
}