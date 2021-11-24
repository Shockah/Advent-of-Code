package pl.shockah.aoc.y2019

import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.geom.polygon.Polygon
import pl.shockah.unikorn.math.MutableVector2
import pl.shockah.unikorn.math.Vector2
import kotlin.math.abs

class Day3: AdventTask<Pair<Polygon, Polygon>, Int, Int>(2019, 3) {
	enum class Direction(
			val symbol: Char,
			val x: Int,
			val y: Int
	) {
		Right('R', 1, 0),
		Up('U', 0, -1),
		Left('L', -1, 0),
		Down('D', 0, 1);

		companion object {
			val bySymbol = values().associateBy { it.symbol }
		}
	}

	override fun parseInput(rawInput: String): Pair<Polygon, Polygon> {
		val lines = rawInput.lines().take(2)

		fun parsePolygon(line: String): Polygon {
			var x = 0
			var y = 0
			val polygon = Polygon()
			polygon.points += MutableVector2(0.0, 0.0)

			for (entry in line.split(",")) {
				val direction = Direction.bySymbol[entry[0]]!!
				val length = entry.drop(1).toInt()

				x += direction.x * length
				y += direction.y * length
				polygon.points += MutableVector2(x.toDouble(), y.toDouble())
			}

			return polygon
		}

		return parsePolygon(lines[0]) to parsePolygon(lines[1])
	}

	enum class Mode {
		ManhattanDistance, Delay
	}

	private fun task(input: Pair<Polygon, Polygon>, mode: Mode): Int {
		val lines = input.first.lines to input.second.lines
		val intersections = mutableListOf<Pair<Vector2, Int>>()

		var length1 = 0
		for (line1 in lines.first) {
			var length2 = 0
			for (line2 in lines.second) {
				(line1 intersect line2)?.let {
					val delay = length1 + length2 + (it - line1.point1).length.toInt() + (it - line2.point1).length.toInt()
					intersections += it to delay
				}
				length2 += line2.perimeter.toInt()
			}
			length1 += line1.perimeter.toInt()
		}

		return when (mode) {
			Mode.ManhattanDistance -> intersections.minOf { (abs(it.first.x) + abs(it.first.y)).toInt() }
			Mode.Delay -> intersections.minOf { it.second }
		}
	}

	override fun part1(input: Pair<Polygon, Polygon>): Int {
		return task(input, Mode.ManhattanDistance)
	}

	override fun part2(input: Pair<Polygon, Polygon>): Int {
		return task(input, Mode.Delay)
	}
}