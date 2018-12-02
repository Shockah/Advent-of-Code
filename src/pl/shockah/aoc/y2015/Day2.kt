package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day2 : AdventTask<List<Day2.Dimensions>, Int, Int>(2015, 2) {
	data class Dimensions(
			val length: Int,
			val width: Int,
			val height: Int
	) {
		val area: Int by lazy {
			2 * (length * width + width * height + height * length)
		}

		val volume: Int by lazy {
			length * width * height
		}

		val sortedSidesByLength: IntArray by lazy {
			listOf(length, width, height).sorted().toIntArray()
		}

		val smallestSide: Int by lazy {
			minOf(length * width, width * height, height * length)
		}

		val shortestSide: Int
			get() = sortedSidesByLength[0]

		val mediumSide: Int
			get() = sortedSidesByLength[1]
	}

	override fun parseInput(rawInput: String): List<Dimensions> {
		return rawInput.lines().map {
			val split = it.split("x").map { it.toInt() }
			return@map Dimensions(split[0], split[1], split[2])
		}
	}

	override fun part1(input: List<Dimensions>): Int {
		return input.map {
			it.area + it.smallestSide
		}.sum()
	}

	override fun part2(input: List<Dimensions>): Int {
		return input.map {
			(it.shortestSide + it.mediumSide) * 2 + it.volume
		}.sum()
	}

	@Suppress("FunctionName")
	class Tests {
		private val task = Day2()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput("2x3x4")
				Assertions.assertEquals(58, task.part1(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("1x1x10")
				Assertions.assertEquals(43, task.part1(input))
			}
		}

		@Nested
		inner class Part2 {
			@Test
			fun `#1`() {
				val input = task.parseInput("2x3x4")
				Assertions.assertEquals(34, task.part2(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("1x1x10")
				Assertions.assertEquals(14, task.part2(input))
			}
		}
	}
}