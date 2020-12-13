package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import kotlin.math.max
import kotlin.math.min

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
		return buses.map { it to it - (input.earliestPossibleDeparture % it) }.minBy { it.second }!!.let { it.first * it.second }
	}

	fun part2(input: Input, minimum: Long): Long {
		fun factorize(value: Int): Map<Int, Int> {
			val results = mutableListOf<Int>()
			var divisor = 2
			var current = value
			while (divisor <= value / 2) {
				if (current % divisor == 0) {
					results += divisor
					current /= divisor
				} else {
					divisor++
				}
			}
			results += current
			return results.groupBy { it }.mapValues { it.value.size }
		}

		val factors = mutableMapOf<Int, Int>()
		for (bus in input.buses.filterNotNull()) {
			val busFactors = factorize(bus)
			for (entry in busFactors) {
				factors[entry.key] = max(factors[entry.key] ?: 0, entry.value)
			}
		}
		val commonFactor = factors.map { it.key.toLong() * it.value.toLong() }.reduce(Long::times)
		println("Common factor: $commonFactor")

		val min = min(commonFactor, minimum)
		println("Minimum: $min")

		var offset = 0
		var currentTotalFactor = 1L
		var nextOffset = input.buses.indexOfFirst { it != null }
		var nextFactor = input.buses[nextOffset]!!.toLong()
		var current = (min.toDouble() / currentTotalFactor.toDouble() + 1).toLong()
		while (true) {
			if (current > min + commonFactor)
				throw IllegalStateException()
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

		private val rawInput = """
			939
			7,13,x,x,59,x,31,19
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(295, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(1068781L, task.part2(input, 1L))
		}
	}
}