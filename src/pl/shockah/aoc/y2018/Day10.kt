package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse4
import pl.shockah.aoc.toCharString
import pl.shockah.unikorn.collection.MutableArray2D
import java.util.regex.Pattern
import kotlin.math.absoluteValue

private const val askForValidity = false

class Day10: AdventTask<List<Day10.Point>, String, Int>(2018, 10) {
	private val inputPattern: Pattern = Pattern.compile("position=<\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*> velocity=<\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*>")

	data class Vector(
		val x: Int,
		val y: Int
	) {
		val manhattanLength: Int
			get() = x.absoluteValue + y.absoluteValue

		operator fun plus(vector: Vector): Vector {
			return Vector(x + vector.x, y + vector.y)
		}

		operator fun minus(vector: Vector): Vector {
			return Vector(x - vector.x, y - vector.y)
		}
	}

	data class Point(
		val position: Vector,
		val velocity: Vector
	) {
		fun advance(): Point {
			return Point(position + velocity, velocity)
		}
	}

	override fun parseInput(rawInput: String): List<Point> {
		return rawInput.lines().map {
			val (positionX, positionY, velocityX, velocityY) = inputPattern.parse4<Int, Int, Int, Int>(it)
			return@map Point(Vector(positionX, positionY), Vector(velocityX, velocityY))
		}
	}

	private fun task(input: List<Point>): Pair<String, Int> {
		fun advance(setup: MutableList<Point>) {
			for (i in 0 until setup.size) {
				setup[i] = setup[i].advance()
			}
		}

		fun isPotentiallyValid(setup: List<Point>): Boolean {
			outer@ for (i in setup.indices) {
				for (j in setup.indices) {
					if (i == j)
						continue
					if ((setup[j].position - setup[i].position).manhattanLength <= 2)
						continue@outer
				}
				return false
			}
			return true
		}

		val setup = input.toMutableList()
		var steps = 0
		while (!isPotentiallyValid(setup)) {
			advance(setup)
			steps++
		}

		println("> Achieved potentially valid state in $steps steps")

		while (true) {
			val minX = setup.minOf { it.position.x }
			val minY = setup.minOf { it.position.y }
			val maxX = setup.maxOf { it.position.x }
			val maxY = setup.maxOf { it.position.y }

			val grid = MutableArray2D(maxX - minX + 1, maxY - minY + 1, false)
			for (point in setup) {
				grid[point.position.x - minX, point.position.y - minY] = true
			}
			val readableOutput = grid.toCharString(false, '.', '#')

			@Suppress("ConstantConditionIf")
			if (askForValidity) {
				println(readableOutput)
				println("> Is this valid?")
				readLine()?.let {
					if (it.lowercase() in arrayOf("true", "t", "yes", "y", "1"))
						return "\n$readableOutput" to steps
				}
			} else {
				return "\n$readableOutput" to steps
			}

			advance(setup)
			steps++
		}
	}

	override fun part1(input: List<Point>): String {
		return task(input).first
	}

	override fun part2(input: List<Point>): Int {
		return task(input).second
	}

	class Tests {
		private val task = Day10()

		private val rawInput = """
			position=< 9,  1> velocity=< 0,  2>
			position=< 7,  0> velocity=<-1,  0>
			position=< 3, -2> velocity=<-1,  1>
			position=< 6, 10> velocity=<-2, -1>
			position=< 2, -4> velocity=< 2,  2>
			position=<-6, 10> velocity=< 2, -2>
			position=< 1,  8> velocity=< 1, -1>
			position=< 1,  7> velocity=< 1,  0>
			position=<-3, 11> velocity=< 1, -2>
			position=< 7,  6> velocity=<-1, -1>
			position=<-2,  3> velocity=< 1,  0>
			position=<-4,  3> velocity=< 2,  0>
			position=<10, -3> velocity=<-1,  1>
			position=< 5, 11> velocity=< 1, -2>
			position=< 4,  7> velocity=< 0, -1>
			position=< 8, -2> velocity=< 0,  1>
			position=<15,  0> velocity=<-2,  0>
			position=< 1,  6> velocity=< 1,  0>
			position=< 8,  9> velocity=< 0, -1>
			position=< 3,  3> velocity=<-1,  1>
			position=< 0,  5> velocity=< 0, -1>
			position=<-2,  2> velocity=< 2,  0>
			position=< 5, -2> velocity=< 1,  2>
			position=< 1,  4> velocity=< 2,  1>
			position=<-2,  7> velocity=< 2, -2>
			position=< 3,  6> velocity=<-1, -1>
			position=< 5,  0> velocity=< 1,  0>
			position=<-6,  0> velocity=< 2,  0>
			position=< 5,  9> velocity=< 1, -2>
			position=<14,  7> velocity=<-2,  0>
			position=<-3,  6> velocity=< 2, -1>
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals("\n" +
					"#...#..###\n" +
					"#...#...#.\n" +
					"#...#...#.\n" +
					"#####...#.\n" +
					"#...#...#.\n" +
					"#...#...#.\n" +
					"#...#...#.\n" +
					"#...#..###", task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(3, task.part2(input))
		}
	}
}