package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

class Day20: AdventTask<Int, Int, Int>(2015, 20) {
	override fun parseInput(rawInput: String): Int {
		return rawInput.toInt()
	}

	private fun task(atLeast: Int, multiplier: Int = 10, limit: Int? = null): Int {
		val presents = IntArray(atLeast / multiplier + 1)

		for (elfIndex in 1..(atLeast / multiplier)) {
			var visited = 0
			for (houseIndex in elfIndex..(atLeast / multiplier) step elfIndex) {
				presents[houseIndex] += elfIndex * multiplier
				if (limit != null) {
					if (++visited >= limit)
						break
				}
			}
		}
		return presents.indexOfFirst { it >= atLeast }
	}

	override fun part1(input: Int): Int {
		return task(input)
	}

	override fun part2(input: Int): Int {
		return task(input, 11, 50)
	}

	class Tests {
		private val task = Day20()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				"150" expects 8
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}
	}
}