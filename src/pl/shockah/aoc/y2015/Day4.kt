package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import java.math.BigInteger
import java.security.MessageDigest

class Day4 : AdventTask<String, Int, Int>(2015, 4) {
	private val digest = MessageDigest.getInstance("MD5")

	override fun parseInput(rawInput: String): String {
		return rawInput
	}

	private val String.md5: String
		get() = BigInteger(1, digest.digest(toByteArray())).toString(16).padStart(32, '0')

	private fun task(input: String, zeros: Int): Int {
		val prefix = "0".repeat(zeros)
		generateSequence(1) { it + 1 }.forEach {
			if ("$input$it".md5.startsWith(prefix))
				return it
		}
		throw IllegalStateException("Unreachable")
	}

	override fun part1(input: String): Int {
		return task(input, 5)
	}

	override fun part2(input: String): Int {
		return task(input, 6)
	}

	@Suppress("FunctionName")
	class Tests {
		private val task = Day4()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput("abcdef")
				Assertions.assertEquals(609043, task.part1(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("pqrstuv")
				Assertions.assertEquals(1048970, task.part1(input))
			}
		}
	}
}