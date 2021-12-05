package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.UnorderedPair

class Day24: AdventTask<List<UnorderedPair<Int>>, Int, Int>(2017, 24) {
	override fun parseInput(rawInput: String): List<UnorderedPair<Int>> {
		return rawInput.trim().lines().map {
			val split = it.split("/")
			return@map UnorderedPair(split[0].toInt(), split[1].toInt())
		}
	}

	override fun part1(input: List<UnorderedPair<Int>>): Int {
		fun calculateMaxStrength(tail: Int, currentStrength: Int, elementsLeft: Set<UnorderedPair<Int>>): Int {
			return elementsLeft
				.filter { it.first == tail || it.second == tail }
				.maxOfOrNull { element ->
					val newTail = if (element.first == tail) element.second else element.first
					return@maxOfOrNull calculateMaxStrength(
						newTail,
						currentStrength + element.first + element.second,
						elementsLeft - element
					)
				} ?: currentStrength
		}

		return calculateMaxStrength(0, 0, input.toSet())
	}

	override fun part2(input: List<UnorderedPair<Int>>): Int {
		data class Result(
			val length: Int,
			val strength: Int
		): Comparable<Result> {
			override fun compareTo(other: Result): Int {
				if (length != other.length)
					return length.compareTo(other.length)
				return strength.compareTo(other.strength)
			}
		}

		fun calculateMaxStrengthOfLongestBridge(tail: Int, currentStrength: Int, currentLength: Int, elementsLeft: Set<UnorderedPair<Int>>): Result {
			return elementsLeft
				.filter { it.first == tail || it.second == tail }
				.maxOfOrNull { element ->
					val newTail = if (element.first == tail) element.second else element.first
					return@maxOfOrNull calculateMaxStrengthOfLongestBridge(
						newTail,
						currentStrength + element.first + element.second,
						currentLength + 1,
						elementsLeft - element
					)
				} ?: Result(currentLength, currentStrength)
		}

		return calculateMaxStrengthOfLongestBridge(0, 0, 0, input.toSet()).strength
	}

	class Tests {
		private val task = Day24()

		private val rawInput = """
			0/2
			2/2
			2/3
			3/4
			3/5
			0/1
			10/1
			9/10
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(31, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(19, task.part2(input))
		}
	}
}