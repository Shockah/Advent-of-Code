package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.collection.MutableArray2D
import java.util.*
import kotlin.math.absoluteValue

class Day6: AdventTask<List<Day6.Point>, Int, Int>(2018, 6) {
	data class Point(
			val x: Int,
			val y: Int
	) {
		val neighbors: Array<Point>
			get() = arrayOf(Point(x - 1, y), Point(x + 1, y), Point(x, y - 1), Point(x, y + 1))
	}

	override fun parseInput(rawInput: String): List<Point> {
		return rawInput.lines().map {
			val split = it.split(", ")
			return@map Point(split[0].toInt(), split[1].toInt())
		}
	}

	private sealed class GridPoint {
		data class Initial(
				val point: Point
		): GridPoint()

		data class Closest(
				val initial: Initial,
				val distance: Int
		): GridPoint()

		data class Ambiguous(
				val distance: Int
		): GridPoint()

		object Empty: GridPoint()
	}

	override fun part1(input: List<Point>): Int {
		val minX = input.map { it.x }.min()!!
		val minY = input.map { it.y }.min()!!
		val maxX = input.map { it.x }.max()!!
		val maxY = input.map { it.y }.max()!!

		val grid = MutableArray2D<GridPoint>(maxX - minX + 3, maxY - minY + 3, GridPoint.Empty)
		val toProcess = LinkedList<Triple<GridPoint.Initial, Point, Int>>()

		for (point in input) {
			val translatedPoint = Point(point.x - minX + 1, point.y - minY + 1)
			val initial = GridPoint.Initial(translatedPoint)
			grid[translatedPoint.x, translatedPoint.y] = initial
			toProcess += Triple(initial, translatedPoint, 0)
		}

		while (!toProcess.isEmpty()) {
			val (initial, point, distance) = toProcess.removeFirst()
			if (point.x < 0 || point.y < 0 || point.x >= grid.width || point.y >= grid.height)
				continue

			when (val existing = grid[point.x, point.y]) {
				GridPoint.Empty -> {
					grid[point.x, point.y] = GridPoint.Closest(initial, distance)
					toProcess += point.neighbors.map { Triple(initial, it, distance + 1) }
				}
				is GridPoint.Ambiguous -> {
					if (existing.distance > distance) {
						grid[point.x, point.y] = GridPoint.Closest(initial, distance)
						toProcess += point.neighbors.map { Triple(initial, it, distance + 1) }
					}
				}
				is GridPoint.Closest -> {
					if (existing.distance > distance) {
						grid[point.x, point.y] = GridPoint.Closest(initial, distance)
						toProcess += point.neighbors.map { Triple(initial, it, distance + 1) }
					} else if (existing.distance == distance && existing.initial != initial) {
						grid[point.x, point.y] = GridPoint.Ambiguous(distance)
					}
				}
				is GridPoint.Initial -> {
					if (existing == initial)
						toProcess += point.neighbors.map { Triple(initial, it, distance + 1) }
				}
			}
		}

		return grid.toMap().filterValues { it is GridPoint.Closest }.mapValues { it.value as GridPoint.Closest }.entries.groupBy { it.value.initial }.filter {
			!it.value.any { it.key.first == 0 || it.key.second == 0 || it.key.first == grid.width - 1 || it.key.second == grid.height - 1 } // going into infinity
		}.values.map { it.size }.max()!! + 1
	}

	fun getLargestSafeRegionSize(input: List<Point>, maxExclusiveTotalDistance: Int): Int {
		val minX = input.map { it.x }.min()!!
		val minY = input.map { it.y }.min()!!
		val maxX = input.map { it.x }.max()!!
		val maxY = input.map { it.y }.max()!!

		val grid = MutableArray2D<Int?>(maxX - minX + 1, maxY - minY + 1, null)
		for (y in 0 until grid.height) {
			for (x in 0 until grid.width) {
				val sum = input.sumBy { (it.x - x).absoluteValue + (it.y - y).absoluteValue }
				if (sum < maxExclusiveTotalDistance)
					grid[x, y] = sum
			}
		}

		val safeRegionSizes = mutableListOf<Int>()
		for (y in 0 until grid.height) {
			for (x in 0 until grid.width) {
				if (grid[x, y] != null) {
					val toProcess = LinkedList<Point>()
					toProcess += Point(x, y)

					var count = 0
					while (!toProcess.isEmpty()) {
						val point = toProcess.removeFirst()
						if (point.x < 0 || point.y < 0 || point.x >= grid.width || point.y >= grid.height)
							continue

						if (grid[point.x, point.y] != null) {
							count++
							toProcess += point.neighbors
							grid[point.x, point.y] = null
						}
					}
					safeRegionSizes += count
				}
			}
		}
		return safeRegionSizes.max()!!
	}

	override fun part2(input: List<Point>): Int {
		return getLargestSafeRegionSize(input, 10_000)
	}

	class Tests {
		private val task = Day6()

		private val rawInput = """
			1, 1
			1, 6
			8, 3
			3, 4
			5, 5
			8, 9
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(17, task.part1(input))
		}

		@Test
		fun getLargestSafeRegionSize() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(16, task.getLargestSafeRegionSize(input, 32))
		}
	}
}