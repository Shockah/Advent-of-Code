package pl.shockah.aoc.y2016

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.collection.Array2D

class Day2: AdventTask<List<List<Day2.Direction>>, String, String>(2016, 2) {
	enum class Direction(
			val x: Int,
			val y: Int,
			val symbol: Char
	) {
		Right(1, 0, 'R'),
		Up(0, -1, 'U'),
		Left(-1, 0, 'L'),
		Down(0, 1, 'D');

		companion object {
			val bySymbol = values().associateBy { it.symbol }
		}
	}

	override fun parseInput(rawInput: String): List<List<Direction>> {
		return rawInput.lines().map { it.toCharArray().map { Direction.bySymbol[it]!! } }
	}

	private fun task(input: List<List<Direction>>, keypad: Array2D<Char?>, initialX: Int, initialY: Int): String {
		val builder = StringBuilder()
		var x = initialX
		var y = initialY
		input.forEach { line ->
			line.forEach directions@ {
				val newX = x + it.x
				val newY = y + it.y

				if (newX < 0 || newY < 0 || newX >= keypad.width || newY >= keypad.height)
					return@directions
				keypad[newX, newY]?.let {
					x = newX
					y = newY
				}
			}
			builder.append(keypad[x, y]!!.toString())
		}
		return builder.toString()
	}

	override fun part1(input: List<List<Direction>>): String {
		val keypad = Array2D<Char?>(3, 3) { x, y -> '0' + (y * 3 + x + 1) }
		return task(input, keypad, 1, 1)
	}

	override fun part2(input: List<List<Direction>>): String {
		val keypad = Array2D(5, 5) { x, y ->
			return@Array2D when (y) {
				0 -> if (x == 2) '1' else null
				1 -> if (x in 1..3) '2' + (x - 1) else null
				2 -> '5' + x
				3 -> if (x in 1..3) 'A' + (x - 1) else null
				4 -> if (x == 2) 'D' else null
				else -> throw IllegalArgumentException()
			}
		}
		return task(input, keypad, 0, 2)
	}

	class Tests {
		private val task = Day2()

		private val rawInput = """
			ULL
			RRDDD
			LURDL
			UUUUD
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals("1985", task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals("5DB3", task.part2(input))
		}
	}
}