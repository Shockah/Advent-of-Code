package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

private val alphabet = "abcdefghijklmnopqrstuvwxyz"

private val Char.next: Char
	get() = alphabet[(alphabet.indexOf(this) + 1) % alphabet.length]

private val String.isAmbiguous: Boolean
	get() = contains('i') || contains('o') || contains('l')

private val String.containsThreeStraight: Boolean
	get() {
		for (i in 2 until length) {
			if (this[i - 2] + 1 == this[i - 1] && this[i - 1] + 1 == this[i])
				return true
		}
		return false
	}

private val String.hasTwoPairs: Boolean
	get() {
		var firstPair: Int? = null
		for (i in 1 until length) {
			if (this[i - 1] == this[i]) {
				firstPair = i
				break
			}
		}

		if (firstPair == null)
			return false

		for (i in firstPair + 2 until length) {
			if (this[i - 1] == this[i])
				return true
		}
		return false
	}

private val String.isValidPassword: Boolean
	get() = !isAmbiguous && containsThreeStraight && hasTwoPairs

private val String.next: String
	get() {
		val result = StringBuilder(this)
		for (i in length - 1 downTo 0) {
			result[i] = result[i].next
			if (result[i] != 'a')
				return result.toString()
		}
		throw IllegalArgumentException("Reached last password.")
	}

class Day11 : AdventTask<String, String, String>(2015, 11) {
	override fun parseInput(rawInput: String): String {
		return rawInput
	}

	override fun part1(input: String): String {
		var current = input
		while (true) {
			current = current.next
			if (current.isValidPassword)
				return current
		}
	}

	override fun part2(input: String): String {
		return part1(part1(input))
	}

	class Tests {
		private val task = Day11()

		@TestFactory
		fun passwordNotAmbiguous(): Collection<DynamicTest> = createTestCases(
				"hijklmmn" expects false,
				"abbceffg" expects true,
				"abbcegjk" expects true
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, !input.isAmbiguous)
		}

		@TestFactory
		fun passwordContrainsThreeStraight(): Collection<DynamicTest> = createTestCases(
				"hijklmmn" expects true,
				"abbceffg" expects false,
				"abbcegjk" expects false
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, input.containsThreeStraight)
		}

		@TestFactory
		fun passwordHasTwoPairs(): Collection<DynamicTest> = createTestCases(
				"hijklmmn" expects false,
				"abbceffg" expects true,
				"abbcegjk" expects false
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, input.hasTwoPairs)
		}

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				"abcdefgh" expects "abcdffaa",
				"ghijklmn" expects "ghjaabcc"
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}
	}
}