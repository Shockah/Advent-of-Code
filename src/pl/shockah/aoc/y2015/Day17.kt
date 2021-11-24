package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day17: AdventTask<List<Day17.Container>, Int, Int>(2015, 17) {
	class Container(
			val capacity: Int
	) {
		override fun toString(): String {
			return "$capacity"
		}
	}

	override fun parseInput(rawInput: String): List<Container> {
		return rawInput.lines().map { Container(it.toInt()) }.sortedByDescending { it.capacity }
	}

	private fun getCombinations(containers: List<Container>, stored: Set<Container>, toStore: Int): Set<Set<Container>> {
		if (toStore == 0)
			return setOf(stored)
		if (containers.isEmpty())
			return setOf()

		val filteredContainers = containers.filter { it.capacity <= toStore && it.capacity <= (stored.lastOrNull()?.capacity ?: it.capacity) }
		return filteredContainers.flatMap { getCombinations(filteredContainers - it, stored + it, toStore - it.capacity) }.toSet()
	}

	private fun part1(input: List<Container>, toStore: Int): Int {
		return getCombinations(input, setOf(), toStore).size
	}

	override fun part1(input: List<Container>): Int {
		return part1(input, 150)
	}

	private fun part2(input: List<Container>, toStore: Int): Int {
		return getCombinations(input, setOf(), toStore).groupBy { it.size }.values.minByOrNull { it[0].size }!!.size
	}

	override fun part2(input: List<Container>): Int {
		return part2(input, 150)
	}

	class Tests {
		private val task = Day17()

		private val rawInput = "20\n15\n10\n5\n5"

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(4, task.part1(input, 25))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(3, task.part2(input, 25))
		}
	}
}