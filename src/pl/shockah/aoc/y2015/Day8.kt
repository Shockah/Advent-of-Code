package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask

class Day8 : AdventTask<List<String>, Int, Int>(2015, 8) {
	override fun parseInput(rawInput: String): List<String> {
		return rawInput.lines()
	}

	override fun part1(input: List<String>): Int {
		return input.map {
			val base = it.substring(0, it.length - 1).substring(1)
			val stripped = StringBuilder()
			val builder = StringBuilder()

			for (c in base) {
				if (c == '\\') {
					builder.append(c)
					if (builder.toString() == "\\\\") {
						stripped.append('\\')
						builder.clear()
					}
				} else if (c == '"') {
					if (builder.toString() == "\\") {
						stripped.append('"')
						builder.clear()
					}
				} else {
					if (c == 'x' && builder.toString() == "\\") {
						builder.append('x')
					} else if ((c in '0'..'9' || c in 'a'..'f') && builder.startsWith("\\x")) {
						builder.append(c)
						if (builder.length == 4) {
							stripped.append(builder.substring(2).toInt(16).toChar())
							builder.clear()
						}
					} else {
						if (!builder.isEmpty()) {
							stripped.append(builder)
							builder.clear()
						}
						stripped.append(c)
					}
				}
			}

			return@map base.length - stripped.length + 2
		}.sum()
	}

	override fun part2(input: List<String>): Int {
		TODO()
	}

	class Tests {
		private val task = Day8()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(listOf(
				Case("\"\"", 2),
				Case("\"abc\"", 2),
				Case("\"aaa\\\"aaa\"", 3),
				Case("\"\\x27\"", 5)
		)) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}
	}
}