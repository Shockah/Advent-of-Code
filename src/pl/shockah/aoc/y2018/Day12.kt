package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse2
import java.util.*
import java.util.regex.Pattern

class Day12 : AdventTask<Day12.Input, Long, Long>(2018, 12) {
	private val replacementInputPattern: Pattern = Pattern.compile("([.#]+) => ([.#])")

	data class Input(
			val state: Set<Int>,
			val combinations: List<Pair<BooleanArray, Boolean>>
	)

	override fun parseInput(rawInput: String): Input {
		val lines = LinkedList(rawInput.lines())

		val rawInitialState = lines.removeFirst().replace("initial state: ", "")
		val initialState = rawInitialState.mapIndexed { index: Int, c: Char -> index to (c == '#') }.filter { it.second }.map { it.first }.toSet()
		lines.removeFirst()

		val replacements = lines.map {
			val (rawCombination, rawCombinationOutput) = replacementInputPattern.parse2<String, Char>(it)
			return@map booleanArrayOf(*rawCombination.map { it == '#' }.toBooleanArray()) to (rawCombinationOutput == '#')
		}

		return Input(initialState, replacements)
	}

	private fun task(input: Input, iterations: Long): Long {
		fun advance(input: Set<Int>, combinations: List<Pair<BooleanArray, Boolean>>): Set<Int> {
			val minX = input.min()!!
			val maxX = input.max()!!
			val maxCombinationLength = combinations.map { it.first.size }.max()!!
			val maxOffset = (maxCombinationLength - 1) / 2

			val output = mutableSetOf<Int>()
			positions@ for (x in (minX - maxOffset)..(maxX + maxOffset)) {
				combinations@ for ((combination, combinationOutput) in combinations) {
					val combinationLength = combination.size
					val combinationOffset = (combinationLength - 1) / 2
					for (i in 0 until combinationLength) {
						if ((x - combinationOffset + i) in input != combination[i])
							continue@combinations
					}
					if (combinationOutput)
						output += x
					continue@positions
				}
			}

			return output
		}

		fun getStringState(input: Set<Int>, withMinMax: Boolean): String {
			val minX = input.min()!!
			val maxX = input.max()!!
			val joined = (minX..maxX).map { if (it in input) '#' else '.' }.joinToString("")
			return if (withMinMax) "[$minX] $joined [$maxX]" else joined
		}

		var current = input.state
		var lastStringState: String? = null
		println("> #0: ${getStringState(current, true)}")

		var currentMod = 1L
		for (i in 1..iterations) {
			val newState = advance(current, input.combinations)
			val newStringState = getStringState(newState, false)
			val areEqual = lastStringState != null && lastStringState == newStringState
			val sumDifference = newState.sum() - current.sum()

			if (i % currentMod == 0L || areEqual) {
				println("> #$i: ${getStringState(newState, true)}")
				if (i >= currentMod * 100)
					currentMod *= 10L
			}
			current = newState
			lastStringState = newStringState

			if (areEqual) {
				println("> Finishing early")
				println("> Current sum: ${current.sum()}")
				println("> Iterations left: ${iterations - i}, ${if (sumDifference > 0) "+" else ""}$sumDifference each")
				return current.sum() + (iterations - i) * sumDifference
			}
		}
		return current.sum().toLong()
	}

	override fun part1(input: Input): Long {
		return task(input, 20L)
	}

	override fun part2(input: Input): Long {
		return task(input, 50_000_000_000L)
	}

	class Tests {
		private val task = Day12()

		private val rawInput = """
			initial state: #..#.#..##......###...###

			...## => #
			..#.. => #
			.#... => #
			.#.#. => #
			.#.## => #
			.##.. => #
			.#### => #
			#.#.# => #
			#.### => #
			##.#. => #
			##.## => #
			###.. => #
			###.# => #
			####. => #
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(325, task.part1(input))
		}
	}
}