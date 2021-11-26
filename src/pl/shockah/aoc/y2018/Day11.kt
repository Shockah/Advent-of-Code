package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.createRawPart2TestCases
import pl.shockah.aoc.expects
import pl.shockah.unikorn.collection.Array2D

class Day11: AdventTask<Array2D<Int>, String, String>(2018, 11) {
	override fun parseInput(rawInput: String): Array2D<Int> {
		val gridSerialNumber = rawInput.toInt()
		return Array2D(300, 300) { x, y ->
			val rackId = x + 11
			var power = (rackId * (y + 1) + gridSerialNumber) * rackId
			power = (if (power < 100) 0 else (power / 100) % 10) - 5
			return@Array2D power
		}
	}

	private fun task(input: Array2D<Int>, squareSize: Int, best: Int? = null): Pair<Pair<Int, Int>, Int>? {
		if (best != null && squareSize * squareSize * 4 <= best)
			return null

		fun getTotalPower(x: Int, y: Int, best: Int? = null): Int? {
			var sum = 0
			var left = squareSize * squareSize
			for (yy in y until (y + squareSize)) {
				for (xx in x until (x + squareSize)) {
					if (best != null && sum + left * 4 < best)
						return null
					sum += input[xx, yy]
					left--
				}
			}
			return sum
		}

		var ourBest: Int? = best
		var bestLocation: Pair<Int, Int>? = null

		for (y in 0 until (input.height - squareSize - 1)) {
			for (x in 0 until (input.width - squareSize - 1)) {
				getTotalPower(x, y, ourBest)?.let { total ->
					if (ourBest == null || total > ourBest!!) {
						ourBest = total
						bestLocation = x to y
					}
				}
			}
		}

		return if (bestLocation == null) null else bestLocation!! to ourBest!!
	}

	private fun task(input: Array2D<Int>): Pair<Triple<Int, Int, Int>, Int> {
		var best: Int? = null
		var bestLocation: Triple<Int, Int, Int>? = null

		for (squareSize in 1..input.width) {
			println("> Finding best for square size $squareSize")
			task(input, squareSize, best)?.let { (point, total) ->
				val (x, y) = point
				println(">> Found best: ${x + 1},${y + 1} == $total")
				best = total
				bestLocation = Triple(x, y, squareSize)
			}
		}
		return bestLocation!! to best!!
	}

	override fun part1(input: Array2D<Int>): String {
		val (x, y) = task(input, 3)!!.first
		return "${x + 1},${y + 1}"
	}

	override fun part2(input: Array2D<Int>): String {
		val (x, y, squareSize) = task(input).first
		return "${x + 1},${y + 1},$squareSize"
	}

	class Tests {
		private val task = Day11()

		@Test
		fun part1() {
			val input = task.parseInput("18")
			Assertions.assertEquals("33,45", task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = task.createRawPart2TestCases(
			"18" expects "90,269,16",
			"42" expects "232,251,12"
		)
	}
}