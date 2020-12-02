package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse4
import java.util.regex.Pattern

class Day2: AdventTask<List<Day2.Entry>, Int, Int>(2020, 2) {
	private val inputPattern = Pattern.compile("(\\d+)-(\\d+) ([a-z]): ([a-z]+)")

	data class Entry(
			val firstNumber: Int,
			val secondNumber: Int,
			val restrictedCharacter: Char,
			val password: String
	)

	override fun parseInput(rawInput: String): List<Entry> {
		return rawInput.lines().map {
			val (firstNumber, secondNumber, restrictedCharacter, password) = inputPattern.parse4<Int, Int, Char, String>(it)
			return@map Entry(firstNumber, secondNumber, restrictedCharacter, password)
		}
	}

	override fun part1(input: List<Entry>): Int {
		return input.count { entry -> entry.password.count { it == entry.restrictedCharacter } in entry.firstNumber..entry.secondNumber }
	}

	override fun part2(input: List<Entry>): Int {
		return input.count { (it.password[it.firstNumber - 1] == it.restrictedCharacter) xor (it.password[it.secondNumber - 1] == it.restrictedCharacter) }
	}

	class Tests {
		private val task = Day2()

		private val rawInput = """
			1-3 a: abcde
			1-3 b: cdefg
			2-9 c: ccccccccc
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(2, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(1, task.part2(input))
		}
	}
}