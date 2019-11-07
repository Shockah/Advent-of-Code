package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse4
import java.util.regex.Pattern

class Day13 : AdventTask<Map<Pair<String, String>, Int>, Int, Int>(2015, 13) {
	private val inputPattern: Pattern = Pattern.compile("(\\w+) would ((?:gain)|(?:lose)) (\\d+) happiness units by sitting next to (\\w+).")

	override fun parseInput(rawInput: String): Map<Pair<String, String>, Int> {
		return rawInput.lines().map {
			val (person1, gainOrLose, happiness, person2) = inputPattern.parse4<String, String, Int, String>(it)
			return@map Pair(person1, person2) to happiness * (if (gainOrLose == "gain") 1 else -1)
		}.toMap()
	}

	private fun getTotalHappiness(values: Map<Pair<String, String>, Int>, table: List<String>): Int {
		var happiness = 0
		for (i in table.indices) {
			happiness += values[Pair(table[i], table[(i + 1) % table.size])] ?: 0
			happiness += values[Pair(table[(i + 1) % table.size], table[i])] ?: 0
		}
		return happiness
	}

	private fun task(input: Map<Pair<String, String>, Int>, includeMyself: Boolean = false): Int {
		val people = (input.keys.map { it.first }.toSet() + input.keys.map { it.second }.toSet()).toMutableSet()
		if (includeMyself)
			people += "Myself!"

		fun getPermutations(table: List<String>, toDistribute: Set<String>): List<List<String>> {
			if (toDistribute.isEmpty())
				return listOf(table)
			return toDistribute.flatMap { getPermutations(table + it, toDistribute - it) }
		}

		return getTotalHappiness(input, getPermutations(listOf(), people).maxBy { getTotalHappiness(input, it) }!!)
	}

	override fun part1(input: Map<Pair<String, String>, Int>): Int {
		return task(input, false)
	}

	override fun part2(input: Map<Pair<String, String>, Int>): Int {
		return task(input, true)
	}

	class Tests {
		private val task = Day13()

		private val rawInput = """
			Alice would gain 54 happiness units by sitting next to Bob.
			Alice would lose 79 happiness units by sitting next to Carol.
			Alice would lose 2 happiness units by sitting next to David.
			Bob would gain 83 happiness units by sitting next to Alice.
			Bob would lose 7 happiness units by sitting next to Carol.
			Bob would lose 63 happiness units by sitting next to David.
			Carol would lose 62 happiness units by sitting next to Alice.
			Carol would gain 60 happiness units by sitting next to Bob.
			Carol would gain 55 happiness units by sitting next to David.
			David would gain 46 happiness units by sitting next to Alice.
			David would lose 7 happiness units by sitting next to Bob.
			David would gain 41 happiness units by sitting next to Carol.
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(330, task.part1(input))
		}
	}
}