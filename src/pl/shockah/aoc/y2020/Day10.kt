package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

class Day10: AdventTask<List<Int>, Int, Long>(2020, 10) {
	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.lines().filter { it.isNotEmpty() }.map { it.toInt() }
	}

	override fun part1(input: List<Int>): Int {
		val sorted = input.sorted()
		val differences = mutableMapOf<Int, Int>()
		differences[sorted[0]] = 1
		differences[3] = 1
		for (i in 1 until sorted.size) {
			val difference = sorted[i] - sorted[i - 1]
			differences[difference] = (differences[difference] ?: 0) + 1
		}
		return differences[1]!! * differences[3]!!
	}

	override fun part2(input: List<Int>): Long {
		val sortedInput = listOf(0) + input.sorted()
		val waysToGetTo = mutableMapOf<Int, Long>()
		for (i in sortedInput.indices) {
			if (i == 0) {
				waysToGetTo[0] = 1
				continue
			}

			var waysToGetToI = 0L
			for (j in (i - 1) downTo 0) {
				if (sortedInput[i] - sortedInput[j] <= 3)
					waysToGetToI += waysToGetTo[j]!!
				else
					break
			}
			waysToGetTo[i] = waysToGetToI
		}
		return waysToGetTo[sortedInput.size - 1]!!
	}

	class Tests {
		private val task = Day10()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
			"16\n10\n15\n5\n1\n11\n7\n19\n6\n12\n4" expects 35,
			"28\n33\n18\n42\n31\n14\n46\n20\n48\n47\n24\n23\n49\n45\n19\n38\n39\n11\n1\n32\n25\n35\n8\n17\n7\n9\n4\n2\n34\n10\n3" expects 220
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
			"3\n6\n9\n12" expects 1L,
			"1\n2\n4" expects 3L,
			"1\n2\n3\n4" expects 7L,
			"16\n10\n15\n5\n1\n11\n7\n19\n6\n12\n4" expects 8L,
			"28\n33\n18\n42\n31\n14\n46\n20\n48\n47\n24\n23\n49\n45\n19\n38\n39\n11\n1\n32\n25\n35\n8\n17\n7\n9\n4\n2\n34\n10\n3" expects 19208L
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}