package pl.shockah.aoc.y2016

import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse2OrNull
import pl.shockah.unikorn.collection.Array2D
import java.util.regex.Pattern

class Day8: AdventTask<List<Day8.Instruction>, Int, String>(2016, 8) {
	private val rectPattern = Pattern.compile("rect (\\d+)x(\\d+)")
	private val rotateRowPattern = Pattern.compile("rotate row y=(\\d+) by (\\d+)")
	private val rotateColumnPattern = Pattern.compile("rotate column x=(\\d+) by (\\d+)")

	sealed class Instruction {
		data class Rect(
			val first: Int,
			val second: Int
		): Instruction()

		data class RotateRow(
			val y: Int,
			val by: Int
		): Instruction()

		data class RotateColumn(
			val x: Int,
			val by: Int
		): Instruction()
	}

	private fun task(input: List<Instruction>): Array2D<Boolean> {
		var screen = Array2D(50, 6, false)
		input.forEach {
			when (it) {
				is Instruction.Rect -> screen = screen.map { _, x, y, element ->
					return@map if (x < it.first && y < it.second) true else element
				}
				is Instruction.RotateRow -> screen = screen.map { array, x, y, element ->
					return@map if (y == it.y) array[(x - it.by + array.width) % array.width, y] else element
				}
				is Instruction.RotateColumn -> screen = screen.map { array, x, y, element ->
					return@map if (x == it.x) array[x, (y - it.by + array.height) % array.height] else element
				}
			}
		}
		return screen
	}

	override fun parseInput(rawInput: String): List<Instruction> {
		return rawInput.lines().map { line ->
			run {
				val parsed = rectPattern.parse2OrNull<Int, Int>(line)
				if (parsed != null) {
					val (first, second) = parsed
					return@map Instruction.Rect(first, second)
				}
			}

			run {
				val parsed = rotateRowPattern.parse2OrNull<Int, Int>(line)
				if (parsed != null) {
					val (y, by) = parsed
					return@map Instruction.RotateRow(y, by)
				}
			}

			run {
				val parsed = rotateColumnPattern.parse2OrNull<Int, Int>(line)
				if (parsed != null) {
					val (x, by) = parsed
					return@map Instruction.RotateColumn(x, by)
				}
			}

			throw IllegalArgumentException()
		}
	}

	override fun part1(input: List<Instruction>): Int {
		return task(input).toList().count { it }
	}

	override fun part2(input: List<Instruction>): String {
		val screen = task(input)
		return (0 until screen.height).joinToString("\n") { y ->
			(0 until screen.width).joinToString("") { x -> if (screen[x, y]) "#" else "." }
		}
	}

	// TODO: tests
}