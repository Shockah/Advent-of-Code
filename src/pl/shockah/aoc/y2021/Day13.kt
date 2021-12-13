package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day13: AdventTask<Day13.Input, Int, String>(2021, 13) {
	data class Input(
		val dots: List<Pair<Int, Int>>,
		val folds: List<Fold>
	) {
		data class Fold(
			val axis: Axis,
			val position: Int
		) {
			enum class Axis {
				X, Y
			}
		}
	}

	override fun parseInput(rawInput: String): Input {
		val rawInputSplit = rawInput.split("\n\n")
		val dotsInput = rawInputSplit[0].trim()
		val foldsInput = rawInputSplit[1].trim()

		val dots = dotsInput.lines().map {
			val split = it.split(',')
			return@map Pair(split[0].toInt(), split[1].toInt())
		}
		val folds = foldsInput.lines().map {
			val split = it.split(' ').last().split('=')
			return@map Input.Fold(if (split[0] == "x") Input.Fold.Axis.X else Input.Fold.Axis.Y, split[1].toInt())
		}
		return Input(dots, folds)
	}

	private fun fold(data: MutableMap<Pair<Int, Int>, Int>, fold: Input.Fold) {
		when (fold.axis) {
			Input.Fold.Axis.X -> {
				val folding = data.keys.filter { it.first >= fold.position }
				folding.forEach {
					if (it.first == fold.position)
						return@forEach
					val target = (2 * fold.position - it.first) to it.second
					data[target] = (data[target] ?: 0) + (data[it] ?: 0)
				}
				folding.forEach { data.remove(it) }
			}
			Input.Fold.Axis.Y -> {
				val folding = data.keys.filter { it.second >= fold.position }
				folding.forEach {
					if (it.second == fold.position)
						return@forEach
					val target = it.first to (2 * fold.position - it.second)
					data[target] = (data[target] ?: 0) + (data[it] ?: 0)
				}
				folding.forEach { data.remove(it) }
			}
		}
	}

	override fun part1(input: Input): Int {
		val data = mutableMapOf<Pair<Int, Int>, Int>()
		input.dots.forEach { data[it] = 1 }
		fold(data, input.folds.first())
		return data.keys.size
	}

	override fun part2(input: Input): String {
		val data = mutableMapOf<Pair<Int, Int>, Int>()
		input.dots.forEach { data[it] = 1 }
		input.folds.forEach { fold(data, it) }

		val keys = data.keys.toSet()
		val minX = keys.minOf { it.first }
		val minY = keys.minOf { it.second }
		val maxX = keys.maxOf { it.first }
		val maxY = keys.maxOf { it.second }
		return (minY .. maxY).joinToString("\n") { y ->
			(minX .. maxX).joinToString("") { x ->
				if ((data[x to y] ?: 0) > 0) "#" else "."
			}
		}
	}

	class Tests {
		private val task = Day13()

		private val rawInput = """
			6,10
			0,14
			9,10
			0,3
			10,4
			4,11
			6,0
			6,12
			4,1
			0,13
			10,12
			3,4
			3,0
			8,4
			1,10
			2,14
			8,10
			9,0
			
			fold along y=7
			fold along x=5
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(17, task.part1(input))
		}
	}
}