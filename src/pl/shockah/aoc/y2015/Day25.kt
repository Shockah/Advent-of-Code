package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.aoc.parse2
import pl.shockah.unikorn.collection.Array2D
import java.util.regex.Pattern

class Day25: AdventTask<Day25.Point, Long, Unit>(2015, 25) {
	private val inputPattern: Pattern = Pattern.compile("row (\\d+), column (\\d+)\\.")

	data class Point(
		val x: Int,
		val y: Int
	)

	override fun parseInput(rawInput: String): Point {
		val (y, x) = inputPattern.parse2<Int, Int>(rawInput)
		return Point(x - 1, y - 1)
	}

	override fun part1(input: Point): Long {
		val values = mutableListOf<Long>()
		values += 20151125L

		fun getIndexForPoint(point: Point): Int {
			var r = 1
			var index = 0
			var x = 0
			var y = 0
			while (true) {
				repeat(r) {
					if (x == point.x && y == point.y)
						return index

					x += 1
					y -= 1
					index++
				}

				y = x
				x = 0
				r++
			}
		}

		fun getValueForIndex(index: Int): Long {
			while (values.size <= index) {
				values += values[values.size - 1] * 252533L % 33554393L
			}
			return values[index]
		}

		return getValueForIndex(getIndexForPoint(input))
	}

	override fun part2(input: Point) {
	}

	class Tests {
		private val task = Day25()

		private val rawExpected = listOf(
			20151125L, 18749137L, 17289845L, 30943339L, 10071777L, 33511524L,
			31916031L, 21629792L, 16929656L, 7726640L, 15514188L, 4041754L,
			16080970L, 8057251L, 1601130L, 7981243L, 11661866L, 16474243L,
			24592653L, 32451966L, 21345942L, 9380097L, 10600672L, 31527494L,
			77061L, 17552253L, 28094349L, 6899651L, 9250759L, 31663883L,
			33071741L, 6796745L, 25397450L, 24659492L, 1534922L, 27995004L
		)

		private val expected = Array2D(6, 6) { x, y -> rawExpected[y * 6 + x] }

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(expected.toMap().map { (p, v) -> Point(p.first, p.second) expects v }) { input, expected ->
			Assertions.assertEquals(expected, task.part1(input))
		}
	}
}