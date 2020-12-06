package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day6: AdventTask<List<Day6.Group>, Int, Int>(2020, 6) {
	data class Group(
		val answerSets: List<Set<Char>>
	)

	override fun parseInput(rawInput: String): List<Group> {
		val groups = rawInput.trim().split("\n\n")
		return groups.map { Group(it.split("\n").map { it.toSet() }) }
	}

	override fun part1(input: List<Group>): Int {
		return input.sumBy { it.answerSets.flatten().toSet().size }
	}

	override fun part2(input: List<Group>): Int {
		return input.sumBy { group -> group.answerSets.flatten().groupBy { it }.count { it.value.size == group.answerSets.size } }
	}

	class Tests {
		private val task = Day6()

		private val rawInput = """
			abc
			
			a
			b
			c
			
			ab
			ac
			
			a
			a
			a
			a
			
			b
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(11, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(6, task.part2(input))
		}
	}
}