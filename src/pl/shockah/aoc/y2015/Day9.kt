package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.aoc.parse3
import java.util.regex.Pattern

class Day9: AdventTask<Map<Day9.UnorderedPair<String>, Int>, Int, Int>(2015, 9) {
	private val inputPattern: Pattern = Pattern.compile("(.*) to (.*) = (\\d+)")

	data class UnorderedPair<T>(
			val first: T,
			val second: T
	) {
		override fun equals(other: Any?): Boolean {
			return other is UnorderedPair<*> && ((first == other.first && second == other.second) || (first == other.second && second == other.first))
		}

		override fun hashCode(): Int {
			return first.hashCode() xor second.hashCode()
		}
	}

	override fun parseInput(rawInput: String): Map<UnorderedPair<String>, Int> {
		return rawInput.lines().map {
			val (from, to, distance) = inputPattern.parse3<String, String, Int>(it)
			return@map UnorderedPair(from, to) to distance
		}.toMap()
	}

	private fun getDistance(distances: Map<UnorderedPair<String>, Int>, path: List<String>): Int {
		var distance = 0
		for (i in 1 until path.size) {
			distance += distances[UnorderedPair(path[i - 1], path[i])]!!
		}
		return distance
	}

	override fun part1(input: Map<UnorderedPair<String>, Int>): Int {
		val locations = input.keys.map { it.first }.toSet() + input.keys.map { it.second }.toSet()

		fun findShortestPath(distances: Map<UnorderedPair<String>, Int>, path: List<String>, available: Set<String>): List<String> {
			if (available.isEmpty())
				return path
			return available.map { findShortestPath(distances, path + it, available - it) }.minBy { getDistance(distances, it) }!!
		}

		return getDistance(input, findShortestPath(input, listOf(), locations))
	}

	override fun part2(input: Map<UnorderedPair<String>, Int>): Int {
		val locations = input.keys.map { it.first }.toSet() + input.keys.map { it.second }.toSet()

		fun findLongestPath(distances: Map<UnorderedPair<String>, Int>, path: List<String>, available: Set<String>): List<String> {
			if (available.isEmpty())
				return path
			return available.map { findLongestPath(distances, path + it, available - it) }.maxBy { getDistance(distances, it) }!!
		}

		return getDistance(input, findLongestPath(input, listOf(), locations))
	}

	class Tests {
		private val task = Day9()

		private val rawInput = """
			London to Dublin = 464
			London to Belfast = 518
			Dublin to Belfast = 141
		""".trimIndent()

		@TestFactory
		fun parseInput(): Collection<DynamicTest> = createTestCases(
				"London to Dublin = 464" expects (UnorderedPair("London", "Dublin") to 464),
				"London to Belfast = 518" expects (UnorderedPair("London", "Belfast") to 518),
				"Dublin to Belfast = 141" expects (UnorderedPair("Dublin", "Belfast") to 141)
		) { rawInput, expected -> Assertions.assertEquals(mapOf(expected), task.parseInput(rawInput)) }

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(605, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(982, task.part2(input))
		}
	}
}