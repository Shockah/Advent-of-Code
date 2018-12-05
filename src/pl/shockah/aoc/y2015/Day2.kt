package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects

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

	class Tests {
		private val task = Day2()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				"2x3x4" expects 58,
				"1x1x10" expects 43
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
				"2x3x4" expects 34,
				"1x1x10" expects 14
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}