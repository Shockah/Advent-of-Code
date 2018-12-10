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

class Day19 : AdventTask<Day19.Input, Int, Int>(2015, 19) {
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
			for (i in 0..(input.length - from.length)) {
				if (input.indexOf(from, i) == i) {
					val builder = StringBuilder(input)
					builder.replace(i, i + from.length, to)
					results += builder.toString()
				}
			}
		}
		return results
	}

	override fun part1(input: Input): Int {
		return getReplacements(input.initialMolecule, input.replacements).size
	}

	override fun part2(input: Input): Int {
		val initialMoleculeLength = input.initialMolecule.moleculeLength
		val toCheck = LinkedList<Pair<String, Int>>()
		val moleculeSet = mutableSetOf<String>()
		toCheck += "e" to 0
		moleculeSet += "e"

		var leastSteps: Int? = null
		var maxSteps = 0
		while (!toCheck.isEmpty()) {
			val (molecule, steps) = toCheck.removeFirst()
			moleculeSet -= molecule
			if (steps > 300) {
				println("> Skipping")
				continue
			}
			if (steps > maxSteps) {
				println("> Got to maxSteps = $maxSteps")
				maxSteps = steps
			}
			if (leastSteps != null && steps >= leastSteps)
				continue
			if (molecule.moleculeLength > initialMoleculeLength) {
				println("> Too long molecule, skipping")
				continue
			}

			if (molecule == input.initialMolecule) {
				println("> Got it after $steps steps")
				if (leastSteps == null || leastSteps > steps) {
					leastSteps = steps
					println(">> And it's the shortest one by far")
				}
				continue
			}

			getReplacements(molecule, input.replacements).forEach { newMolecule ->
				if (newMolecule in moleculeSet) {
					val existingToCheck = toCheck.first { it.first == newMolecule }
					toCheck.remove(existingToCheck)
				} else {
					moleculeSet += newMolecule
				}
				toCheck += newMolecule to (steps + 1)
			}
		}

		return leastSteps!!
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
				"HOH" expects 3,
				"HOHOHO" expects 6
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