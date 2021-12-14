package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day14: AdventTask<Day14.Input, Int, Long>(2021, 14) {
	data class Input(
		val template: String,
		val rules: Map<String, String>
	)

	override fun parseInput(rawInput: String): Input {
		val rawInputSplit = rawInput.trim().split("\n\n")
		val template = rawInputSplit[0].trim()
		val rulesInput = rawInputSplit[1].trim()

		val rules = rulesInput.trim().lines().map {
			val split = it.split("->").map { it.trim() }
			return@map split[0] to split[1]
		}
		return Input(template, rules.toMap())
	}

	private fun tick(polymer: String, rules: Map<String, String>): String {
		val builder = StringBuilder()
		for (i in 1 until polymer.length) {
			builder.append(polymer[i - 1])
			val rule = rules[polymer.substring((i - 1) .. i)] ?: continue
			builder.append(rule)
		}
		builder.append(polymer.last())
		return builder.toString()
	}

	override fun part1(input: Input): Int {
		var current = input.template
		repeat(10) { current = tick(current, input.rules) }
		val symbols = current.toSet()
		val symbolCounts = symbols.map { symbol -> symbol to current.count { it == symbol } }
		val mostCommon = symbolCounts.maxByOrNull { it.second }!!
		val leastCommon = symbolCounts.minByOrNull { it.second }!!
		println("Most common: ${mostCommon.first}: ${mostCommon.second}")
		println("Least common: ${leastCommon.first}: ${leastCommon.second}")
		return mostCommon.second - leastCommon.second
	}

	override fun part2(input: Input): Long {
		TODO()
	}

	class Tests {
		private val task = Day14()

		private val rawInput = """
			NNCB

			CH -> B
			HH -> N
			CB -> H
			NH -> C
			HB -> C
			HC -> B
			HN -> C
			NN -> C
			BH -> H
			NC -> B
			NB -> B
			BN -> B
			BB -> N
			BC -> B
			CC -> N
			CN -> C
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(1588, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(2188189693529L, task.part2(input))
		}
	}
}