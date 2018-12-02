package pl.shockah.aoc.y2015

import pl.shockah.aoc.AdventTask
import java.io.File

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

	override fun parseInput(file: File): List<Dimensions> {
		return file.readLines().filter { !it.isEmpty() }.map {
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
}