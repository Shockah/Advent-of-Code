package pl.shockah.aoc.y2016

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.collection.Array2D

class Day3: AdventTask<Array2D<Int>, Int, Int>(2016, 3) {
	override fun parseInput(rawInput: String): Array2D<Int> {
		val lines = rawInput.lines().map { it.trim() }.filter { !it.isEmpty() }.map { it.split(Regex("\\s+")).map { it.trim().toInt() }}
		return Array2D(3, lines.size) { x, y -> lines[y][x] }
	}

	override fun part1(input: Array2D<Int>): Int {
		return (0 until input.height).count { y ->
			val sorted = input.getRow(y).sorted()
			return@count sorted[0] + sorted[1] > sorted[2]
		}
	}

	override fun part2(input: Array2D<Int>): Int {
		return (0 until input.width).sumBy { x ->
			return@sumBy (0 until (input.height / 3)).count { ySection ->
				val sorted = (0 until 3).map { y -> input[x, ySection * 3 + y] }.sorted()
				return@count sorted[0] + sorted[1] > sorted[2]
			}
		}
	}

	class Tests {
		private val task = Day3()

		@Test
		fun part1() {
			val input = task.parseInput("""
				5 10 25
			""".trimIndent())
			Assertions.assertEquals(0, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput("""
				101 301 501
				102 302 502
				103 303 503
				201 401 601
				202 402 602
				203 403 603
			""".trimIndent())
			Assertions.assertEquals(6, task.part2(input))
		}
	}
}