package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.collection.Array2D

class Day9: AdventTask<Array2D<Int>, Int, Int>(2021, 9) {
	override fun parseInput(rawInput: String): Array2D<Int> {
		val lines = rawInput.trim().lines()
		return Array2D(lines.first().length, lines.size) { x, y -> lines[y][x].digitToInt() }
	}

	private fun getNeighborPoints(input: Array2D<Int>, point: Pair<Int, Int>): Set<Pair<Int, Int>> {
		val neighbors = mutableSetOf<Pair<Int, Int>>()
		if (point.first > 0) neighbors += Pair(point.first - 1, point.second)
		if (point.first < input.width - 1) neighbors += Pair(point.first + 1, point.second)
		if (point.second > 0) neighbors += Pair(point.first, point.second - 1)
		if (point.second < input.height - 1) neighbors += Pair(point.first, point.second + 1)
		return neighbors
	}

	private fun getLowPoints(input: Array2D<Int>): Set<Pair<Int, Int>> {
		return (0 until input.height).flatMap { y ->
			return@flatMap (0 until input.width).mapNotNull { x ->
				val value = input[x, y]
				return@mapNotNull if (getNeighborPoints(input, Pair(x, y)).any { (x, y) -> input[x, y] <= value }) null else Pair(x, y)
			}
		}.toSet()
	}

	private fun getBasin(input: Array2D<Int>, lowPoint: Pair<Int, Int>): Set<Pair<Int, Int>> {
		val checked = mutableSetOf<Pair<Int, Int>>()
		val toCheck = mutableListOf<Pair<Int, Int>>().apply { this += lowPoint }
		val points = mutableSetOf<Pair<Int, Int>>()

		while (toCheck.isNotEmpty()) {
			val point = toCheck.removeFirst()
			if (input[point.first, point.second] == 9)
				continue
			points += point
			toCheck += getNeighborPoints(input, point) - checked
			checked += point
		}

		return points
	}

	override fun part1(input: Array2D<Int>): Int {
		return getLowPoints(input).sumOf { (x, y) -> input[x, y] + 1 }
	}

	override fun part2(input: Array2D<Int>): Int {
		val lowPoints = getLowPoints(input)
		val basins = lowPoints.map { getBasin(input, it) }
		return basins.sortedByDescending { it.size }.take(3).fold(1) { acc, basin -> acc * basin.size }
	}

	class Tests {
		private val task = Day9()

		private val rawInput = """
			2199943210
			3987894921
			9856789892
			8767896789
			9899965678
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(15, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(1134, task.part2(input))
		}
	}
}