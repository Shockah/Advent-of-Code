package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day6: AdventTask<List<Int>, Long, Long>(2021, 6) {
	companion object {
		private const val normalCycle = 6
		private const val newCycle = 8
	}

	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.trim().split(',').map { it.toInt() }
	}

	private fun task(input: List<Int>, cycles: Int): Long {
		val counters = LongArray(newCycle + 1)
		input.forEach { counters[it]++ }

		println("Day 0: ${counters.sum()} fish")
		fun tick(day: Int) {
			val newFish = counters[0]
			(1 .. newCycle).forEach { counters[it - 1] = counters[it] }
			counters[newCycle] = newFish
			counters[normalCycle] += newFish
			println("Day ${day + 1}: ${counters.sum()} fish")
		}

		repeat(cycles) { tick(it) }
		return counters.sum()
	}

	override fun part1(input: List<Int>): Long {
		return task(input, 80)
	}

	override fun part2(input: List<Int>): Long {
		return task(input, 256)
	}

	class Tests {
		private val task = Day6()
		private val rawInput = "3,4,3,1,2"

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(5934L, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(26984457539L, task.part2(input))
		}
	}
}