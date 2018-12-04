package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.aoc.nextInCycle
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
		Down(0, 1)
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
				direction = direction.nextInCycle
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
				direction = direction.nextInCycle
			}
			edgeLength++
		}
	}

	class Tests {
		private val task = Day3()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(listOf(
				"1" expects 0,
				"12" expects 3,
				"23" expects 2,
				"1024" expects 31
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}
	}
}