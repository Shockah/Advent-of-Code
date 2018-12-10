package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.Array2D

class Day18 : AdventTask<Array2D<Boolean>, Int, Int>(2015, 18) {
	override fun parseInput(rawInput: String): Array2D<Boolean> {
		val lines = rawInput.lines()
		return Array2D(lines[0].length, lines.size) { x, y -> lines[y][x] == '#' }
	}

	private fun task(input: Array2D<Boolean>, steps: Int, cornerOn: Boolean): Int {
		fun advance(grid: Array2D<Boolean>): Array2D<Boolean> {
			fun activeNeighbors(x: Int, y: Int): Int {
				return ((y - 1)..(y + 1)).map { yy ->
					((x - 1)..(x + 1)).map isActive@ { xx ->
						if (xx < 0 || yy < 0 || xx >= grid.width || yy >= grid.height || (xx == x && yy == y))
							return@isActive 0
						else
							return@isActive if (grid[xx, yy]) 1 else 0
					}.sum()
				}.sum()
			}

			return Array2D(grid.width, grid.height) { x, y ->
				if (cornerOn && ((x == 0 && (y == 0 || y == grid.height - 1)) || (x == grid.width - 1 && (y == 0 || y == grid.height - 1))))
					return@Array2D true

				if (grid[x, y])
					return@Array2D activeNeighbors(x, y) in 2..3
				else
					return@Array2D activeNeighbors(x, y) == 3
			}
		}

		var current = input
		if (cornerOn) {
			current = Array2D(current.width, current.height) { x, y ->
				if ((x == 0 && (y == 0 || y == current.height - 1)) || (x == current.width - 1 && (y == 0 || y == current.height - 1)))
					return@Array2D true
				else
					return@Array2D current[x, y]
			}
		}

		repeat(steps) {
			current = advance(current)
		}
		return current.toList().filter { it }.size
	}

	override fun part1(input: Array2D<Boolean>): Int {
		return task(input, 100, false)
	}

	override fun part2(input: Array2D<Boolean>): Int {
		return task(input, 100, true)
	}

	class Tests {
		private val task = Day18()

		private val rawInput = """
			.#.#.#
			...##.
			#....#
			..#...
			#.#..#
			####..
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(4, task.task(input, 4, false))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(17, task.task(input, 5, true))
		}
	}
}