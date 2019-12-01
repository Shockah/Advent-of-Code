package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.aoc.parse2
import java.util.*
import java.util.regex.Pattern

private val String.moleculeLength: Int
	get() = count { it.isUpperCase() || it == 'e' }

private fun String.replace(startIndex: Int, endIndex: Int, replacement: String): String {
	val builder = StringBuilder(this)
	builder.replace(startIndex, endIndex, replacement)
	return builder.toString()
}

private fun String.allIndexesOf(find: String): List<Int> {
	val results = mutableListOf<Int>()
	var currentIndex = 0
	while (true) {
		val foundAt = indexOf(find, currentIndex)
		if (foundAt == -1)
			return results

		results += foundAt
		currentIndex = foundAt + find.length
	}
}

class Day19: AdventTask<Day19.Input, Int, Int>(2015, 19) {
	private val replacementInputPattern: Pattern = Pattern.compile("(\\w+) => (\\w+)")

	data class Input(
			val replacements: List<Pair<String, String>>,
			val initialMolecule: String
	)

	override fun parseInput(rawInput: String): Input {
		val lines = LinkedList(rawInput.lines())
		val replacements = mutableListOf<Pair<String, String>>()

		while (true) {
			val line = lines.removeFirst()
			if (line.isEmpty())
				return Input(replacements, lines.removeFirst())

			val (from, to) = replacementInputPattern.parse2<String, String>(line)
			replacements += from to to
		}
	}

	private fun getReplacements(input: String, replacements: List<Pair<String, String>>): Set<String> {
		val results = mutableSetOf<String>()
		for ((from, to) in replacements) {
			for (index in input.allIndexesOf(from)) {
				results += input.replace(index, index + from.length, to)
			}
		}
		return results
	}

	override fun part1(input: Input): Int {
		return getReplacements(input.initialMolecule, input.replacements).size
	}

	override fun part2(input: Input): Int {
		return input.initialMolecule.moleculeLength - (input.initialMolecule.allIndexesOf("Rn").size + input.initialMolecule.allIndexesOf("Y").size) * 2 - 1
	}

	class Tests {
		private val task = Day19()

		@Test
		fun part1() {
			val input = task.parseInput("""
				H => HO
				H => OH
				O => HH

				HOH
			""".trimIndent())
			Assertions.assertEquals(4, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
				"HOH" expects 2,
				"HOHOHO" expects 5
		) { rawInput, expected ->
			val baseInput = """
				e => H
				e => O
				H => HO
				H => OH
				O => HH
			""".trimIndent()

			val input = task.parseInput("$baseInput\n\n$rawInput")
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}