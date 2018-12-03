package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import java.util.regex.Pattern

class Day3: AdventTask<List<Day3.Rectangle>, Int, Int>(2018, 3) {
	private val inputPattern: Pattern = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")

	data class Rectangle(
			val id: Int,
			val x: Int,
			val y: Int,
			val w: Int,
			val h: Int
	)

	override fun parseInput(rawInput: String): List<Rectangle> {
		return rawInput.lines().map {
			val matcher = inputPattern.matcher(it)
			if (!matcher.find()) {
				println(it)
				throw IllegalArgumentException()
			}

			val id = matcher.group(1).toInt()
			val x = matcher.group(2).toInt()
			val y = matcher.group(3).toInt()
			val w = matcher.group(4).toInt()
			val h = matcher.group(5).toInt()
			return@map Rectangle(id, x, y, w, h)
		}
	}

	override fun part1(input: List<Rectangle>): Int {
		val map = mutableMapOf<Pair<Int, Int>, Int>()
		for (rectangle in input) {
			for (yy in 0 until rectangle.h) {
				for (xx in 0 until rectangle.w) {
					val point = Pair(rectangle.x + xx, rectangle.y + yy)
					map[point] = (map[point] ?: 0) + 1
				}
			}
		}

		var sum = 0
		for (y in 0 until 1000) {
			for (x in 0 until 1000) {
				val point = Pair(x, y)
				if (map[point] ?: 0 > 1)
					sum++
			}
		}
		return sum
	}

	override fun part2(input: List<Rectangle>): Int {
		val map = mutableMapOf<Pair<Int, Int>, Int>()
		for (rectangle in input) {
			for (yy in 0 until rectangle.h) {
				for (xx in 0 until rectangle.w) {
					val point = Pair(rectangle.x + xx, rectangle.y + yy)
					map[point] = (map[point] ?: 0) + 1
				}
			}
		}

		outer@ for (rectangle in input) {
			for (yy in 0 until rectangle.h) {
				for (xx in 0 until rectangle.w) {
					val point = Pair(rectangle.x + xx, rectangle.y + yy)
					if (map[point] ?: 0 != 1)
						continue@outer
				}
			}
			return rectangle.id
		}
		throw IllegalArgumentException()
	}

	@Suppress("FunctionName")
	class Tests {
		private val task = Day3()

		private val rawInput = """
			#1 @ 1,3: 4x4
			#2 @ 3,1: 4x4
			#3 @ 5,5: 2x2
		""".trimIndent()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput(rawInput)
				Assertions.assertEquals(4, task.part1(input))
			}
		}

		@Nested
		inner class Part2 {
			@Test
			fun `#1`() {
				val input = task.parseInput(rawInput)
				Assertions.assertEquals(3, task.part2(input))
			}
		}
	}
}