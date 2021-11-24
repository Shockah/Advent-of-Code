package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.collection.Array2D

class Day11: AdventTask<Array2D<Day11.Tile>, Int, Int>(2020, 11) {
	@Suppress("unused")
	enum class Tile(
		val symbol: Char
	) {
		Floor('.'), Empty('L'), Occupied('#');

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

	override fun part1(input: Array2D<Tile>): Int {
		var current = input
		while (true) {
			val new = current.map { array, x, y, element ->
				val neighborIndexes = Array2D.cardinalAndDiagonalNeighborIndexes.map { it.first + x to it.second + y }.toSet()
				return@map when (element) {
					Tile.Empty -> if (array.count(neighborIndexes) { it == Tile.Occupied } == 0) Tile.Occupied else element
					Tile.Occupied -> if (array.count(neighborIndexes) { it == Tile.Occupied } >= 4) Tile.Empty else element
					Tile.Floor -> element
				}
			}
			if (new == current)
				break
			current = new
		}
		return current.toList().count { it == Tile.Occupied }
	}

	override fun part2(input: Array2D<Tile>): Int {
		var current = input
		while (true) {
			val new = current.map { array, x, y, element ->
				fun firstSeatInDirection(direction: Pair<Int, Int>): Tile? {
					var xx = x
					var yy = y
					while (true) {
						xx += direction.first
						yy += direction.second
						if (xx !in 0 until array.width || yy !in 0 until array.height)
							break
						val tile = array[xx, yy]
						if (tile != Tile.Floor)
							return tile
					}
					return null
				}

				fun getSeatCountInAllDirections(matchingTile: Tile): Int {
					return Array2D.cardinalAndDiagonalNeighborIndexes.count { firstSeatInDirection(it) == matchingTile }
				}

				return@map when (element) {
					Tile.Empty -> if (getSeatCountInAllDirections(Tile.Occupied) == 0) Tile.Occupied else element
					Tile.Occupied -> if (getSeatCountInAllDirections(Tile.Occupied) >= 5) Tile.Empty else element
					Tile.Floor -> element
				}
			}
			if (new == current)
				break
			current = new
		}
		return current.toList().count { it == Tile.Occupied }
	}

	class Tests {
		private val task = Day11()

		private val rawInput = """
			L.LL.LL.LL
			LLLLLLL.LL
			L.L.L..L..
			LLLL.LL.LL
			L.LL.LL.LL
			L.LLLLL.LL
			..L.L.....
			LLLLLLLLLL
			L.LLLLLL.L
			L.LLLLL.LL
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(37, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(26, task.part2(input))
		}
	}
}