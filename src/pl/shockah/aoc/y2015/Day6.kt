package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.IntPatternParser
import pl.shockah.aoc.parse
import java.util.regex.Pattern

class Day6 : AdventTask<List<Day6.Instruction>, Int, Int>(2015, 6) {
	private val inputPattern: Pattern = Pattern.compile("((?:turn on)|(?:turn off)|(?:toggle)) (\\d+),(\\d+) through (\\d+),(\\d+)")

	enum class Operation(
			val inputName: String,
			val action: (set: MutableSet<Pair<Int, Int>>, point: Pair<Int, Int>) -> Unit
	) {
		TurnOn("turn on", { set, point ->
			set += point
		}),
		TurnOff("turn off", { set, point ->
			set -= point
		}),
		Toggle("toggle", { set, point ->
			if (point in set)
				set -= point
			else
				set += point
		});

		operator fun invoke(set: MutableSet<Pair<Int, Int>>, point: Pair<Int, Int>) {
			action(set, point)
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
					{ Operation.values().first { operation -> operation.inputName == it } },
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
					instruction.operation(lightsOn, Pair(x, y))
				}
			}
		}
		return lightsOn.size
	}

	override fun part2(input: List<Instruction>): Int {
		TODO()
	}

	class Tests {
		private val task = Day6()

		private val rawInput = """
			turn on 0,0 through 999,999
			toggle 0,0 through 999,0
			turn off 499,499 through 500,500
		""".trimIndent()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(listOf(
				Case(rawInput.lines().take(1).joinToString("\n"), 1_000_000), // lines 1-1
				Case(rawInput.lines().take(2).joinToString("\n"), 999_000), // lines 1-2
				Case(rawInput.lines().take(3).joinToString("\n"), 998_996), // lines 1-3
				Case(rawInput.lines().drop(1).take(1).joinToString("\n"), 1_000) // lines 2-2
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}
	}
}