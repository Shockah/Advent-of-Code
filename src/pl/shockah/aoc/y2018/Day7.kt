package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse2
import java.util.regex.Pattern

class Day7 : AdventTask<Map<String, Day7.Step>, String, Int>(2018, 7) {
	private val inputPattern: Pattern = Pattern.compile("Step (\\w+) must be finished before step (\\w+) can begin.")

	data class Step(
			val name: String,
			val requires: List<Step>
	) {
		val requiredBy: MutableList<Step> = mutableListOf()
	}

	override fun parseInput(rawInput: String): Map<String, Step> {
		val entries = mutableMapOf<String, MutableList<String>>()
		rawInput.lines().forEach {
			val (required, requiring) = inputPattern.parse2<String, String>(it)
			if (entries[required] == null)
				entries[required] = mutableListOf()
			if (entries[requiring] == null)
				entries[requiring] = mutableListOf()
			entries[requiring]!! += required
		}

		val steps = mutableMapOf<String, Step>()
		while (!entries.isEmpty()) {
			val oldCount = entries.size
			val iterator = entries.iterator()
			outer@ while (iterator.hasNext()) {
				val (requiringName, requiredNameList) = iterator.next()
				val requiredList = mutableListOf<Step>()
				for (requiredName in requiredNameList) {
					requiredList += steps[requiredName] ?: continue@outer
				}

				val step = Step(requiringName, requiredList)
				steps[step.name] = step
				iterator.remove()
			}

			if (oldCount == entries.size)
				throw IllegalArgumentException("Cycle between steps $steps $entries.")
		}

		for (step in steps.values) {
			step.requires.forEach {
				it.requiredBy += step
			}
		}
		return steps
	}

	override fun part1(input: Map<String, Step>): String {
		val completed = mutableListOf<Step>()
		val left = input.values.toMutableList()

		while (!left.isEmpty()) {
			val availableNow = left.filter { completed.containsAll(it.requires) }.sortedBy { it.name }.first()
			completed += availableNow
			left -= availableNow
		}

		return completed.joinToString("") { it.name }
	}

	override fun part2(input: Map<String, Step>): Int {
		TODO()
	}

	class Tests {
		private val task = Day7()

		private val rawInput = """
			Step C must be finished before step A can begin.
			Step C must be finished before step F can begin.
			Step A must be finished before step B can begin.
			Step A must be finished before step D can begin.
			Step B must be finished before step E can begin.
			Step D must be finished before step E can begin.
			Step F must be finished before step E can begin.
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals("CABDFE", task.part1(input))
		}
	}
}