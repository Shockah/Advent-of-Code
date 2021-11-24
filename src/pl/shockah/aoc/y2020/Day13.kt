package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

class Day13: AdventTask<Day13.Input, Int, Long>(2020, 13) {
	data class Input(
		val earliestPossibleDeparture: Int,
		val buses: List<Int?>
	)

	override fun parseInput(rawInput: String): Input {
		val lines = rawInput.lines().filter { it.isNotEmpty() }
		return Input(
			lines[0].toInt(),
			lines[1].split(",").map { if (it == "x") null else it.toInt() }
		)
	}

	override fun part1(input: Input): Int {
		val buses = input.buses.filterNotNull()
		return buses.map { it to it - (input.earliestPossibleDeparture % it) }.minByOrNull { it.second }!!.let { it.first * it.second }
	}

	fun part2(input: Input, minimum: Long): Long {
		var offset: Int
		var currentTotalFactor = 1L
		var nextOffset = input.buses.indexOfFirst { it != null }
		var nextFactor = input.buses[nextOffset]!!.toLong()
		var current = minimum
		while (true) {
			if ((current + nextOffset) % nextFactor == 0L) {
				println("Found value $current for factors $currentTotalFactor and $nextFactor, new factor ${currentTotalFactor * nextFactor}")
				currentTotalFactor *= nextFactor
				offset = nextOffset

				val nextOffsetIndex = input.buses.drop(offset + 1).indexOfFirst { it != null }
				if (nextOffsetIndex == -1)
					return current
				nextOffset = offset + nextOffsetIndex + 1
				nextFactor = input.buses[nextOffset]!!.toLong()
			} else {
				current += currentTotalFactor
			}
		}
	}

	override fun part2(input: Input): Long {
		return part2(input, 100000000000000L)
	}

	class Tests {
		private val task = Day13()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
			"939\n7,13,x,x,59,x,31,19" expects 295
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
			"939\n7,13,x,x,59,x,31,19" expects 1068781L,
			"0\n17,x,13,19" expects 3417L,
			"0\n67,7,59,61" expects 754018L,
			"0\n67,x,7,59,61" expects 779210L,
			"0\n67,7,x,59,61" expects 1261476L,
			"0\n1789,37,47,1889" expects 1202161486L
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input, 1L))
		}
	}
}