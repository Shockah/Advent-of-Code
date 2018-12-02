package pl.shockah.aoc.y2016

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import kotlin.math.absoluteValue

class Day1 : AdventTask<List<Day1.Instruction>, Int, Int>(2016, 1) {
	enum class Turn {
		Left, Right
	}

	data class Instruction(
			val turn: Turn,
			val steps: Int
	)

	private enum class Direction(
			val x: Int,
			val y: Int
	) {
		Right(1, 0),
		Up(0, -1),
		Left(-1, 0),
		Down(0, 1);

		val clockwise: Direction
			get() = Direction.values()[(ordinal + Direction.values().size - 1) % Direction.values().size]

		val counterClockwise: Direction
			get() = Direction.values()[(ordinal + 1) % Direction.values().size]

		fun getAfterTurn(turn: Turn): Direction {
			return if (turn == Turn.Left) counterClockwise else clockwise
		}
	}

	override fun parseInput(rawInput: String): List<Instruction> {
		return rawInput.split(", ").map {
			val direction = if (it[0] == 'L') Turn.Left else Turn.Right
			val steps = it.substring(1).toInt()
			return@map Instruction(direction, steps)
		}
	}

	private enum class Mode {
		All, FirstVisitedTwice
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun task(input: List<Instruction>, mode: Mode): Int {
		var x = 0
		var y = 0
		var direction = Direction.Up
		val visited = mutableSetOf<Pair<Int, Int>>()

		for (instruction in input) {
			direction = direction.getAfterTurn(instruction.turn)

			repeat(instruction.steps) {
				x += direction.x
				y += direction.y

				if (mode == Mode.FirstVisitedTwice) {
					val point = Pair(x, y)
					if (point in visited)
						return x.absoluteValue + y.absoluteValue
					visited += point
				}
			}
		}

		return when (mode) {
			Mode.All -> x.absoluteValue + y.absoluteValue
			Mode.FirstVisitedTwice -> throw IllegalArgumentException("Never visited any location twice.")
		}
	}

	override fun part1(input: List<Instruction>): Int {
		return task(input, Mode.All)
	}

	override fun part2(input: List<Instruction>): Int {
		return task(input, Mode.FirstVisitedTwice)
	}

	@Suppress("FunctionName")
	class Tests {
		private val task = Day1()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput("R2, L3")
				Assertions.assertEquals(5, task.part1(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("R2, R2, R2")
				Assertions.assertEquals(2, task.part1(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("R5, L5, R5, R3")
				Assertions.assertEquals(12, task.part1(input))
			}
		}

		@Nested
		inner class Part2 {
			@Test
			fun `#1`() {
				val input = task.parseInput("R8, R4, R4, R8")
				Assertions.assertEquals(4, task.part2(input))
			}
		}
	}
}