package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse5
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
			val (id, x, y, w, h) = inputPattern.parse5<Int, Int, Int, Int, Int>(it)
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

		return map.values.filter { it > 1 }.size
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
					if (map[point] != 1)
						continue@outer
				}
			}
			return rectangle.id
		}
		throw IllegalArgumentException()
	}

	class Tests {
		private val task = Day3()

		private val rawInput = """
			#1 @ 1,3: 4x4
			#2 @ 3,1: 4x4
			#3 @ 5,5: 2x2
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(4, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(3, task.part2(input))
		}
	}
}