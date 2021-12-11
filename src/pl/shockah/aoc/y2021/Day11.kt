package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.collection.Array2D
import pl.shockah.unikorn.collection.MutableArray2D

class Day11: AdventTask<Array2D<Int>, Int, Int>(2021, 11) {
	private val neighborOffsets = listOf(
		-1 to -1, 0 to -1, 1 to -1,
		-1 to 0, 1 to 0,
		-1 to 1, 0 to 1, 1 to 1
	)

	override fun parseInput(rawInput: String): Array2D<Int> {
		val lines = rawInput.trim().lines()
		return Array2D(lines.first().length, lines.size) { x, y -> lines[y][x].digitToInt() }
	}

	private fun getNeighborPoints(input: Array2D<Int>, point: Pair<Int, Int>): Set<Pair<Int, Int>> {
		return neighborOffsets.mapNotNull {
			val neighbor = (point.first + it.first) to (point.second + it.second)
			return@mapNotNull if (neighbor.first < 0 || neighbor.second < 0 || neighbor.first >= input.width || neighbor.second >= input.height) null else neighbor
		}.toSet()
	}

	private fun tick(input: MutableArray2D<Int>): Int {
		var flashes = 0
		val flashesToCheck = mutableListOf<Pair<Int, Int>>()

		fun tickUp(x: Int, y: Int) {
			if (input[x, y] == 9) {
				flashes++
				flashesToCheck += x to y
			}
			input[x, y]++
		}

		for (y in 0 until input.height) {
			for (x in 0 until input.width) {
				tickUp(x, y)
			}
		}
		while (flashesToCheck.isNotEmpty()) {
			val flash = flashesToCheck.removeFirst()
			getNeighborPoints(input, flash).forEach { tickUp(it.first, it.second) }
		}
		for (y in 0 until input.height) {
			for (x in 0 until input.width) {
				if (input[x, y] >= 10)
					input[x, y] = 0
			}
		}

		return flashes
	}

	override fun part1(input: Array2D<Int>): Int {
		val mutableInput = MutableArray2D(input.width, input.height) { x, y -> input[x, y] }
		var flashes = 0
		repeat(100) { flashes += tick(mutableInput) }
		return flashes
	}

	override fun part2(input: Array2D<Int>): Int {
		val mutableInput = MutableArray2D(input.width, input.height) { x, y -> input[x, y] }
		var steps = 0
		while (true) {
			if (mutableInput.toList().toSet().size == 1)
				return steps
			steps++
			tick(mutableInput)
		}
	}

	class Tests {
		private val task = Day11()

		private val rawInput = """
			5483143223
			2745854711
			5264556173
			6141336146
			6357385478
			4167524645
			2176841721
			6882881134
			4846848554
			5283751526
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(1656, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(195, task.part2(input))
		}
	}
}