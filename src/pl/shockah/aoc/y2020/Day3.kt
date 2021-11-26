package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.collection.Array2D

class Day3: AdventTask<Array2D<Day3.Tile>, Int, Long>(2020, 3) {
	@Suppress("unused")
	enum class Tile(
		val symbol: Char
	) {
		Empty('.'), Tree('#');

		companion object {
			val bySymbol = values().associateBy { it.symbol }
		}
	}

	override fun parseInput(rawInput: String): Array2D<Tile> {
		val lines = rawInput.lines()
		return Array2D(lines[0].length, lines.size) { x, y ->
			return@Array2D Tile.bySymbol.getValue(lines[y][x])
		}
	}

	private fun calculateTreeCount(input: Array2D<Tile>, right: Int, down: Int): Int {
		var x = 0
		var y = 0
		var treeCount = 0

		do {
			if (input[x % input.width, y] == Tile.Tree)
				treeCount++
			x += right
			y += down
		} while (y < input.height)
		return treeCount
	}

	override fun part1(input: Array2D<Tile>): Int {
		return calculateTreeCount(input, 3, 1)
	}

	override fun part2(input: Array2D<Tile>): Long {
		return listOf(
			calculateTreeCount(input, 1, 1),
			calculateTreeCount(input, 3, 1),
			calculateTreeCount(input, 5, 1),
			calculateTreeCount(input, 7, 1),
			calculateTreeCount(input, 1, 2)
		).map { it.toLong() }.reduce(Long::times)
	}

	class Tests {
		private val task = Day3()

		private val rawInput = """
			..##.......
			#...#...#..
			.#....#..#.
			..#.#...#.#
			.#...##..#.
			..#.##.....
			.#.#.#....#
			.#........#
			#.##...#...
			#...##....#
			.#..#...#.#
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(7, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(336, task.part2(input))
		}
	}
}