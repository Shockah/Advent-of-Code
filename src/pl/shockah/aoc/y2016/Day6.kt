package pl.shockah.aoc.y2016

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day6: AdventTask<List<String>, String, String>(2016, 6) {
	override fun parseInput(rawInput: String): List<String> {
		return rawInput.trim().lines()
			.also { require(it.map { it.length }.toSet().size == 1) }
	}

	override fun part1(input: List<String>): String {
		return (0 until input[0].length).map { index ->
			return@map input.groupBy { it[index] }.toList().sortedByDescending { it.second.size }[0].first
		}.toCharArray().let { String(it) }
	}

	override fun part2(input: List<String>): String {
		return (0 until input[0].length).map { index ->
			return@map input.groupBy { it[index] }.toList().sortedBy { it.second.size }[0].first
		}.toCharArray().let { String(it) }
	}

	class Tests {
		private val task = Day6()

		private val rawInput = """
			eedadn
			drvtee
			eandsr
			raavrd
			atevrs
			tsrnev
			sdttsa
			rasrtv
			nssdts
			ntnada
			svetve
			tesnvt
			vntsnd
			vrdear
			dvrsen
			enarar
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals("easter", task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals("advent", task.part2(input))
		}
	}
}