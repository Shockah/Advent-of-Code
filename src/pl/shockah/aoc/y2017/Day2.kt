package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day2: AdventTask<List<List<Int>>, Int, Int>(2017, 2) {
	override fun parseInput(rawInput: String): List<List<Int>> {
		return rawInput.lines().map {
			it.trim().split(inputSplitPattern).map { it.toInt() }
		}
	}

	override fun part1(input: List<List<Int>>): Int {
		return input.sumOf { it.maxOrNull()!! - it.minOrNull()!! }
	}

	override fun part2(input: List<List<Int>>): Int {
		return input.map { it.sorted() }.map {
			var divided = 0
			for (i in it.indices) {
				for (j in i until it.size) {
					if (it[j] != it[i] && it[j] % it[i] == 0) {
						require(divided == 0) { "Invalid input data (extra matching values)." }
						divided = it[j] / it[i]
					}
				}
				if (divided != 0)
					return@map divided
			}
			throw IllegalArgumentException("Invalid input data (no matching values).")
		}.sum()
	}

	class Tests {
		private val task = Day2()

		@Test
		fun part1() {
			val input = task.parseInput("""
					5 1 9 5
					7 5 3
					2 4 6 8
				""".trimIndent())
			Assertions.assertEquals(18, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput("""
					5 9 2 8
					9 4 7 3
					3 8 6 5
				""".trimIndent())
			Assertions.assertEquals(9, task.part2(input))
		}
	}
}