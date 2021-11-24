package pl.shockah.aoc.y2016

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.aoc.parse2OrNull
import pl.shockah.unikorn.Ref
import java.util.regex.Pattern

class Day9: AdventTask<String, Int, Long>(2016, 9) {
	private sealed class DataPart {
		abstract fun length(): Long

		data class Data(
			val data: String
		): DataPart() {
			override fun length(): Long {
				return data.length.toLong()
			}
		}

		data class Repeat(
			val parts: List<DataPart>,
			val times: Long
		): DataPart() {
			override fun length(): Long {
				return parts.sumOf { it.length() } * times
			}
		}
	}

	private val markerPattern = Pattern.compile("^\\((\\d+)x(\\d+)\\)")

	override fun parseInput(rawInput: String): String {
		return rawInput.trim()
	}

	override fun part1(input: String): Int {
		val builder = StringBuilder()
		var index = 0
		while (index < input.length) {
			val parsedMarker = markerPattern.parse2OrNull<Int, Int>(input.drop(index))
			if (parsedMarker != null) {
				val (characterCount, times) = parsedMarker
				index += "(${characterCount}x${times})".length
				builder.append(input.drop(index).take(characterCount).repeat(times))
				index += characterCount
				continue
			}

			builder.append(input[index++])
		}
		return builder.length
	}

	override fun part2(input: String): Long {
		return DataPart.Repeat(parseAllDataParts(input), 1).length()
	}

	private fun parseAllDataParts(input: String, index: Ref<Int> = Ref(0)): List<DataPart> {
		val parts = mutableListOf<DataPart>()
		while (index.value < input.length) {
			parts += parseDataPart(input, index)
		}
		return parts
	}

	private fun parseDataPart(input: String, index: Ref<Int>): DataPart {
		when (val markerIndex = input.indexOf('(', index.value)) {
			index.value -> {
				val (characterCount, times) = markerPattern.parse2OrNull<Int, Long>(input.drop(index.value))!!
				index.value += "(${characterCount}x${times})".length
				val result = DataPart.Repeat(parseAllDataParts(input.drop(index.value).take(characterCount)), times)
				index.value += characterCount
				return result
			}
			-1 -> {
				val result = DataPart.Data(input.drop(index.value))
				index.value = input.length
				return result
			}
			else -> {
				val result = DataPart.Data(input.substring(index.value, markerIndex))
				index.value += result.data.length
				return result
			}
		}
	}

	class Tests {
		private val task = Day9()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
			"ADVENT" expects 6,
			"A(1x5)BC" expects 7,
			"(3x3)XYZ" expects 9,
			"A(2x2)BCD(2x2)EFG" expects 11,
			"(6x1)(1x3)A" expects 6,
			"X(8x2)(3x3)ABCY" expects 18
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
			"(3x3)XYZ" expects 9L,
			"X(8x2)(3x3)ABCY" expects 20L,
			"(27x12)(20x12)(13x14)(7x10)(1x12)A" expects 241920L,
			"(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN" expects 445L
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part2(input))
		}
	}
}