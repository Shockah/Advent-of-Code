package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day2: AdventTask<List<Day2.Instruction>, Int, Int>(2021, 2) {
	data class Instruction(
		val direction: Direction,
		val length: Int
	)

	enum class Direction(
		val x: Int,
		val y: Int
	) {
		Forward(1, 0),
		Down(0, 1),
		Up(0, -1)
	}

	override fun parseInput(rawInput: String): List<Instruction> {
		return rawInput.trim().lines().map {
			val split = it.split(" ")
			val direction = when (split[0]) {
				"forward" -> Direction.Forward
				"down" -> Direction.Down
				"up" -> Direction.Up
				else -> throw IllegalArgumentException()
			}
			val length = split[1].toInt()
			return@map Instruction(direction, length)
		}
	}

	override fun part1(input: List<Instruction>): Int {
		var x = 0
		var y = 0
		input.forEach {
			x += it.direction.x * it.length
			y += it.direction.y * it.length
		}
		return x * y
	}

	override fun part2(input: List<Instruction>): Int {
		var aim = 0
		var x = 0
		var y = 0
		input.forEach {
			when (it.direction) {
				Direction.Down -> aim += it.length
				Direction.Up -> aim -= it.length
				Direction.Forward -> {
					x += it.length
					y += aim * it.length
				}
			}
		}
		return x * y
	}

	class Tests {
		private val task = Day2()

		private val rawInput = """
			forward 5
			down 5
			forward 8
			up 3
			down 8
			forward 2
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(150, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(900, task.part2(input))
		}
	}
}