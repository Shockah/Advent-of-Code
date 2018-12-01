package pl.shockah.aoc.y2017

import pl.shockah.aoc.AdventTask
import java.io.File

class Day5: AdventTask<List<Int>, Int, Int>(2017, 5) {
	override fun parseInput(file: File): List<Int> {
		return file.readLines().map { it.toInt() }
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun task(input: List<Int>, part2Rule: Boolean): Int {
		val instructions = input.toMutableList()
		var index = 0
		var steps = 0

		while (true) {
			if (index !in 0 until instructions.size)
				return steps

			val jump = instructions[index]

			if (part2Rule && jump >= 3)
				instructions[index]--
			else
				instructions[index]++

			index += jump
			steps++
		}
	}

	override fun part1(input: List<Int>): Int {
		return task(input, false)
	}

	override fun part2(input: List<Int>): Int {
		return task(input, true)
	}
}