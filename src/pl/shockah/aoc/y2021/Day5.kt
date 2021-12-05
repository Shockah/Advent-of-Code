package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.UnorderedPair
import kotlin.math.max
import kotlin.math.min

class Day5: AdventTask<List<UnorderedPair<Day5.Point>>, Int, Int>(2021, 5) {
	data class Point(
		val x: Int,
		val y: Int
	)

	override fun parseInput(rawInput: String): List<UnorderedPair<Point>> {
		return rawInput.trim().lines().map {
			val split = it.split("->").map { it.trim().split(',').map { it.toInt() } }
			return@map UnorderedPair(Point(split[0][0], split[0][1]), Point(split[1][0], split[1][1]))
		}
	}

	private fun printGrid(grid: Map<Point, Int>, lines: List<UnorderedPair<Point>>) {
		val minX = lines.minOf { min(it.first.x, it.second.x) }
		val minY = lines.minOf { min(it.first.y, it.second.y) }
		val maxX = lines.maxOf { max(it.first.x, it.second.x) }
		val maxY = lines.maxOf { max(it.first.y, it.second.y) }

		println(
			(minY .. maxY).joinToString("\n") { y ->
				(minX .. maxX).joinToString("") { x ->
					grid[Point(x, y)]?.let { "$it" } ?: "."
				}
			}
		)
	}

	private fun task(input: List<UnorderedPair<Point>>, includeDiagonals: Boolean): Int {
		val grid = mutableMapOf<Point, Int>()
		for (line in input) {
			val minX = min(line.first.x, line.second.x)
			val minY = min(line.first.y, line.second.y)
			val maxX = max(line.first.x, line.second.x)
			val maxY = max(line.first.y, line.second.y)

			if (minX == maxX) {
				(minY .. maxY).forEach {
					val point = Point(line.first.x, it)
					grid[point] = (grid[point] ?: 0) + 1
				}
			} else if (minY == maxY) {
				(minX .. maxX).forEach {
					val point = Point(it, line.first.y)
					grid[point] = (grid[point] ?: 0) + 1
				}
			} else if (includeDiagonals) {
				(0 .. (maxX - minX)).forEach {
					val point = Point(
						line.first.x + (if (line.second.x > line.first.x) it else -it),
						line.first.y + (if (line.second.y > line.first.y) it else -it)
					)
					grid[point] = (grid[point] ?: 0) + 1
				}
			}
		}
		return grid.values.count { it >= 2 }
	}

	override fun part1(input: List<UnorderedPair<Point>>): Int {
		return task(input, includeDiagonals = false)
	}

	override fun part2(input: List<UnorderedPair<Point>>): Int {
		return task(input, includeDiagonals = true)
	}

	class Tests {
		private val task = Day5()

		private val rawInput = """
			0,9 -> 5,9
			8,0 -> 0,8
			9,4 -> 3,4
			2,2 -> 2,1
			7,0 -> 7,4
			6,4 -> 2,0
			0,9 -> 2,9
			3,4 -> 1,4
			0,0 -> 8,8
			5,5 -> 8,2
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(5, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(12, task.part2(input))
		}
	}
}