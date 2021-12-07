package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

class Day7: AdventTask<List<Int>, Int, Int>(2021, 7) {
	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.trim().split(',').map { it.toInt() }
	}

	override fun part1(input: List<Int>): Int {
		val sorted = input.sorted()
		val position = sorted[input.size / 2]
		return sorted.sumOf { abs(it - position) }
	}

	override fun part2(input: List<Int>): Int {
		val average = input.average()
		val positions = arrayOf(floor(average).toInt(), ceil(average).toInt())
		return positions.minOf { position ->
			return@minOf input.sumOf {
				val n = abs(it - position)
				return@sumOf n * (1 + n) / 2
			}
		}
	}

	class Tests {
		private val task = Day7()
		private val rawInput = "16,1,2,0,4,2,7,1,2,14"

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(37, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(168, task.part2(input))
		}
	}
}