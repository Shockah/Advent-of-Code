package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.nextInCycle
import pl.shockah.unikorn.previousInCycle
import kotlin.math.abs

class Day12: AdventTask<List<Day12.Instruction>, Int, Int>(2020, 12) {
	data class Instruction(
		val type: InstructionType,
		val value: Int
	)

	enum class InstructionType(
		val symbol: Char
	) {
		North('N'), South('S'), East('E'), West('W'), Left('L'), Right('R'), Forward('F');

		companion object {
			val bySymbol = values().associateBy { it.symbol }
		}
	}

	enum class Direction(
		val x: Int,
		val y: Int
	) {
		East(1, 0),
		North(0, -1),
		West(-1, 0),
		South(0, 1);

		val clockwise: Direction
			get() = previousInCycle

		val counterClockwise: Direction
			get() = nextInCycle
	}

	override fun parseInput(rawInput: String): List<Instruction> {
		return rawInput.lines().filter { it.isNotEmpty() }.map {
			val type = InstructionType.bySymbol[it[0]]!!
			val value = it.drop(1).toInt()
			return@map Instruction(type, value)
		}
	}

	override fun part1(input: List<Instruction>): Int {
		var direction = Direction.East
		var x = 0
		var y = 0
		input.forEach { instruction ->
			when (instruction.type) {
				InstructionType.North, InstructionType.South, InstructionType.East, InstructionType.West -> {
					val movingDirection = when (instruction.type) {
						InstructionType.North -> Direction.North
						InstructionType.South -> Direction.South
						InstructionType.East -> Direction.East
						InstructionType.West -> Direction.West
						else -> throw IllegalStateException()
					}
					x += movingDirection.x * instruction.value
					y += movingDirection.y * instruction.value
				}
				InstructionType.Forward -> {
					x += direction.x * instruction.value
					y += direction.y * instruction.value
				}
				InstructionType.Left, InstructionType.Right -> {
					repeat(instruction.value / 90) {
						if (instruction.type == InstructionType.Left)
							direction = direction.counterClockwise
						else
							direction = direction.clockwise
					}
				}
			}
		}
		return abs(x) + abs(y)
	}

	override fun part2(input: List<Instruction>): Int {
		var waypointX = 10
		var waypointY = -1
		var shipX = 0
		var shipY = 0
		input.forEach { instruction ->
			when (instruction.type) {
				InstructionType.North, InstructionType.South, InstructionType.East, InstructionType.West -> {
					val movingDirection = when (instruction.type) {
						InstructionType.North -> Direction.North
						InstructionType.South -> Direction.South
						InstructionType.East -> Direction.East
						InstructionType.West -> Direction.West
						else -> throw IllegalStateException()
					}
					waypointX += movingDirection.x * instruction.value
					waypointY += movingDirection.y * instruction.value
				}
				InstructionType.Forward -> {
					shipX += waypointX * instruction.value
					shipY += waypointY * instruction.value
				}
				InstructionType.Left, InstructionType.Right -> {
					repeat(instruction.value / 90) {
						if (instruction.type == InstructionType.Left) {
							val (newX, newY) = waypointY to -waypointX
							waypointX = newX
							waypointY = newY
						} else {
							val (newX, newY) = -waypointY to waypointX
							waypointX = newX
							waypointY = newY
						}
					}
				}
			}
		}
		return abs(shipX) + abs(shipY)
	}

	class Tests {
		private val task = Day12()

		private val rawInput = """
			F10
			N3
			F7
			R90
			F11
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(25, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(286, task.part2(input))
		}
	}
}