package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.IntPatternParser
import pl.shockah.aoc.expects
import pl.shockah.aoc.parse
import pl.shockah.aoc.y2015.Day6.Operation.*
import pl.shockah.unikorn.collection.MutableArray2D
import java.util.regex.Pattern
import kotlin.math.max

class Day6: AdventTask<List<Day6.Instruction>, Int, Int>(2015, 6) {
	private val inputPattern: Pattern = Pattern.compile("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)")

	enum class Operation(
			val inputName: String,
			val part1Action: (set: MutableSet<Pair<Int, Int>>, point: Pair<Int, Int>) -> Unit,
			val part2Action: (map: MutableArray2D<Int>, point: Pair<Int, Int>) -> Unit
	) {
		TurnOn("turn on", { set, point ->
			set += point
		}, { map, (x, y) ->
			map[x, y]++
		}),
		TurnOff("turn off", { set, point ->
			set -= point
		}, { map, (x, y) ->
			map[x, y] = max(map[x, y] - 1, 0)
		}),
		Toggle("toggle", { set, point ->
			if (point in set)
				set -= point
			else
				set += point
		}, { map, (x, y) ->
			map[x, y] += 2
		});

		companion object {
			val byInputName = values().associateBy { it.inputName }
		}
	}

	data class Instruction(
			val operation: Operation,
			val x1: Int,
			val y1: Int,
			val x2: Int,
			val y2: Int
	)

	override fun parseInput(rawInput: String): List<Instruction> {
		return rawInput.lines().map {
			val (operation, x1, y1, x2, y2) = inputPattern.parse(
					it,
					{ Operation.byInputName[it]!! },
					IntPatternParser,
					IntPatternParser,
					IntPatternParser,
					IntPatternParser
			)
			return@map Instruction(operation, x1, y1, x2, y2)
		}
	}

	override fun part1(input: List<Instruction>): Int {
		val lightsOn = mutableSetOf<Pair<Int, Int>>()
		for (instruction in input) {
			for (y in instruction.y1..instruction.y2) {
				for (x in instruction.x1..instruction.x2) {
					instruction.operation.part1Action(lightsOn, Pair(x, y))
				}
			}
		}
		return lightsOn.size
	}

	override fun part2(input: List<Instruction>): Int {
		val lights = MutableArray2D(1000, 1000, 0)
		for (instruction in input) {
			for (y in instruction.y1..instruction.y2) {
				for (x in instruction.x1..instruction.x2) {
					instruction.operation.part2Action(lights, Pair(x, y))
				}
			}
		}
		return lights.toList().sum()
	}

	class Tests {
		private val task = Day6()

		@TestFactory
		fun parseInput(): Collection<DynamicTest> = createTestCases(
				"turn on 0,0 through 999,999" expects Instruction(TurnOn, 0, 0, 999, 999),
				"toggle 0,0 through 999,0" expects Instruction(Toggle, 0, 0, 999, 0),
				"turn off 499,499 through 500,500" expects Instruction(TurnOff, 499, 499, 500, 500)
		) { rawInput, expected -> Assertions.assertEquals(listOf(expected), task.parseInput(rawInput)) }

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				listOf(Instruction(TurnOn, 0, 0, 999, 999)) expects 1_000_000,
				listOf(Instruction(TurnOn, 0, 0, 999, 999), Instruction(Toggle, 0, 0, 999, 0)) expects 999_000,
				listOf(Instruction(TurnOn, 0, 0, 999, 999), Instruction(Toggle, 0, 0, 999, 0), Instruction(TurnOff, 499, 499, 500, 500)) expects 998_996,
				listOf(Instruction(Toggle, 0, 0, 999, 0)) expects 1_000
		) { input, expected -> Assertions.assertEquals(expected, task.part1(input)) }

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
				Instruction(TurnOn, 0, 0, 0, 0) expects 1,
				Instruction(Toggle, 0, 0, 999, 999) expects 2_000_000,
				Instruction(TurnOff, 0, 0, 0, 0) expects 0
		) { input, expected -> Assertions.assertEquals(expected, task.part2(listOf(input))) }
	}
}