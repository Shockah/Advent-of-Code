package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import kotlin.math.absoluteValue

class Day3: AdventTask<Int, Int, Int>(2017, 3) {
	override fun parseInput(rawInput: String): Int {
		return rawInput.toInt()
	}

	private enum class Direction(
			val x: Int,
			val y: Int
	) {
		Right(1, 0),
		Up(0, -1),
		Left(-1, 0),
		Down(0, 1);

		val next: Direction
			get() = Direction.values()[(ordinal + 1) % Direction.values().size]
	}

	override fun part1(input: Int): Int {
		var x = 0
		var y = 0
		var index = 1
		var direction = Direction.Right
		var edgeLength = 1

		if (input == 1)
			return 0

		while (true) {
			repeat(2) {
				repeat(edgeLength) {
					x += direction.x
					y += direction.y
					index++

					if (index == input)
						return x.absoluteValue + y.absoluteValue
				}
				direction = direction.next
			}
			edgeLength++
		}
	}

	override fun part2(input: Int): Int {
		val memory = mutableMapOf<Pair<Int, Int>, Int>()
		memory[Pair(0, 0)] = 1

		var x = 0
		var y = 0
		var direction = Direction.Right
		var edgeLength = 1

		while (true) {
			repeat(2) {
				repeat(edgeLength) {
					x += direction.x
					y += direction.y

					var sum = 0
					for (yy in -1..1) {
						for (xx in -1..1) {
							if (xx == 0 && yy == 0)
								continue
							sum += memory[Pair(x + xx, y + yy)] ?: 0
						}
					}

					if (sum > input)
						return sum
					memory[Pair(x, y)] = sum
				}
				direction = direction.next
			}
			edgeLength++
		}
	}

	@Suppress("FunctionName")
	class Tests {
		private val task = Day3()

		@Nested
		inner class Part1 {
			@Test
			fun `#1`() {
				val input = task.parseInput("1")
				Assertions.assertEquals(0, task.part1(input))
			}

			@Test
			fun `#2`() {
				val input = task.parseInput("12")
				Assertions.assertEquals(3, task.part1(input))
			}

			@Test
			fun `#3`() {
				val input = task.parseInput("23")
				Assertions.assertEquals(2, task.part1(input))
			}

			@Test
			fun `#4`() {
				val input = task.parseInput("1024")
				Assertions.assertEquals(31, task.part1(input))
			}
		}
	}
}