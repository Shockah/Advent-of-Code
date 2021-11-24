package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.jay.JSONList
import pl.shockah.jay.JSONObject
import pl.shockah.jay.JSONParseException
import pl.shockah.jay.JSONParser
import java.math.BigInteger

class Day12: AdventTask<Any, Int, Int>(2015, 12) {
	override fun parseInput(rawInput: String): Any {
		return try {
			JSONParser().parseObject(rawInput)
		} catch (e: JSONParseException) {
			JSONParser().parseList(rawInput)
		}
	}

	private fun sum(json: Any, ignoredValue: String? = null): Int {
		return when (json) {
			is JSONObject -> {
				if (ignoredValue != null && json.values.contains(ignoredValue))
					0
				else
					json.values.sumOf { sum(it, ignoredValue) }
			}
			is JSONList<*> -> json.sumOf { sum(it, ignoredValue) }
			is BigInteger -> json.intValueExact()
			else -> 0
		}
	}

	override fun part1(input: Any): Int {
		return sum(input, null)
	}

	override fun part2(input: Any): Int {
		return sum(input, "red")
	}

	class Tests {
		private val task = Day12()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				"[1,2,3]" expects 6,
				"{\"a\":2,\"b\":4}" expects 6,
				"[[[3]]]" expects 3,
				"{\"a\":{\"b\":4},\"c\":-1}" expects 3,
				"[]" expects 0,
				"{}" expects 0
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
				"[1,2,3]" expects 6,
				"[1,{\"c\":\"red\",\"b\":2},3]" expects 4,
				"{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}" expects 0,
				"[1,\"red\",5]" expects 6
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}