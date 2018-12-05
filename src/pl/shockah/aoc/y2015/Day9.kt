package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse3
import java.util.regex.Pattern

class Day9 : AdventTask<Map<Day9.UnorderedPair<String>, Int>, Int, Int>(2015, 9) {
	private val inputPattern: Pattern = Pattern.compile("")

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

	private fun findShortestPathLength()

	override fun part1(input: Map<UnorderedPair<String>, Int>): Int {
		val locations = input.keys.map { it.first }.toSet() + input.keys.map { it.second }.toSet()
	}

	override fun part2(input: Map<UnorderedPair<String>, Int>): Int {
		TODO()
	}

	class Tests {
		private val task = Day9()

		private val rawInput = """
			London to Dublin = 464
			London to Belfast = 518
			Dublin to Belfast = 141
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(605, task.part1(input))
		}
	}
}