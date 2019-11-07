package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.aoc.parse2
import java.util.*
import java.util.regex.Pattern

@ExperimentalUnsignedTypes
class Day7 : AdventTask<Map<String, Day7.Input>, Int, Int>(2015, 7) {
	private val baseInputPattern: Pattern = Pattern.compile("(.*) -> (\\w+)")
	private val notInputPattern: Pattern = Pattern.compile("NOT (\\w+)")
	private val twoArgumentInputPattern: Pattern = Pattern.compile("(\\w+) ((?:AND)|(?:OR)|(?:LSHIFT)|(?:RSHIFT)) (\\w+)")

	data class InputTemplate(
			val name: String,
			val leftSide: String,
			val requiredInputs: List<String>
	)

	interface Input {
		fun getValue(map: Map<String, Input>): Int

		abstract class Cacheable : Input {
			protected var cached: Int? = null

			fun clearCache() {
				cached = null
			}
		}
	}

	data class Signal(
			val value: Int
	) : Input {
		override fun getValue(map: Map<String, Input>): Int {
			return value
		}
	}

	data class AnotherInput(
			val name: String
	) : Input {
		override fun getValue(map: Map<String, Input>): Int {
			return map[name]!!.getValue(map)
		}
	}

	data class Not(
			val input: Input
	) : Input.Cacheable() {
		override fun getValue(map: Map<String, Input>): Int {
			if (cached == null)
				cached = input.getValue(map).toUShort().inv().toInt()
			return cached!!
		}
	}

	data class And(
			val input1: Input,
			val input2: Input
	) : Input.Cacheable() {
		override fun getValue(map: Map<String, Input>): Int {
			if (cached == null)
				cached = (input1.getValue(map).toUShort() and input2.getValue(map).toUShort()).toInt()
			return cached!!
		}
	}

	data class Or(
			val input1: Input,
			val input2: Input
	) : Input.Cacheable() {
		override fun getValue(map: Map<String, Input>): Int {
			if (cached == null)
				cached = (input1.getValue(map).toUShort() or input2.getValue(map).toUShort()).toInt()
			return cached!!
		}
	}

	data class LeftShift(
			val input1: Input,
			val input2: Input
	) : Input.Cacheable() {
		override fun getValue(map: Map<String, Input>): Int {
			if (cached == null)
				cached = (input1.getValue(map) shl input2.getValue(map)).toUShort().toInt()
			return cached!!
		}
	}

	data class RightShift(
			val input1: Input,
			val input2: Input
	) : Input.Cacheable() {
		override fun getValue(map: Map<String, Input>): Int {
			if (cached == null)
				cached = (input1.getValue(map) shr input2.getValue(map)).toUShort().toInt()
			return cached!!
		}
	}

	private fun preParseInput(rawInput: String): List<InputTemplate> {
		return rawInput.lines().map {
			val (leftSide, name) = baseInputPattern.parse2<String, String>(it)
			val requiredInputs = mutableListOf<String>()

			var matcher = twoArgumentInputPattern.matcher(leftSide)
			if (matcher.find()) {
				if (matcher.group(1).toIntOrNull() == null)
					requiredInputs += matcher.group(1)
				if (matcher.group(3).toIntOrNull() == null)
					requiredInputs += matcher.group(3)
			} else {
				matcher = notInputPattern.matcher(leftSide)
				if (matcher.find()) {
					if (matcher.group(1).toIntOrNull() == null)
						requiredInputs += matcher.group(1)
				} else {
					if (leftSide.toIntOrNull() == null)
						requiredInputs += leftSide
				}
			}

			return@map InputTemplate(name, leftSide, requiredInputs)
		}
	}

	private fun parseInput(preInput: List<InputTemplate>): Map<String, Input> {
		val map = mutableMapOf<String, Input>()
		val list = LinkedList(preInput)

		while (!list.isEmpty()) {
			val oldCount = list.size

			val iterator = list.iterator()
			while (iterator.hasNext()) {
				val template = iterator.next()
				if (map.keys.containsAll(template.requiredInputs)) {
					var matcher = twoArgumentInputPattern.matcher(template.leftSide)
					if (matcher.find()) {
						val leftGroup = matcher.group(1)
						val rightGroup = matcher.group(3)
						val leftInput = if (leftGroup.toIntOrNull() == null) AnotherInput(leftGroup) else Signal(leftGroup.toInt())
						val rightInput = if (rightGroup.toIntOrNull() == null) AnotherInput(rightGroup) else Signal(rightGroup.toInt())

						map[template.name] = when (matcher.group(2)) {
							"AND" -> And(leftInput, rightInput)
							"OR" -> Or(leftInput, rightInput)
							"LSHIFT" -> LeftShift(leftInput, rightInput)
							"RSHIFT" -> RightShift(leftInput, rightInput)
							else -> throw IllegalArgumentException("Unknown operation ${matcher.group(2)}.")
						}
					} else {
						matcher = notInputPattern.matcher(template.leftSide)
						if (matcher.find()) {
							val group = matcher.group(1)
							val input = if (group.toIntOrNull() == null) AnotherInput(group) else Signal(group.toInt())
							map[template.name] = Not(input)
						} else {
							val group = template.leftSide
							val input = if (group.toIntOrNull() == null) AnotherInput(group) else Signal(group.toInt())
							map[template.name] = input
						}
					}

					iterator.remove()
				}
			}

			require(oldCount != list.size) { "Found a cycle." }
		}

		return map
	}

	override fun parseInput(rawInput: String): Map<String, Input> {
		return parseInput(preParseInput(rawInput))
	}

	override fun part1(input: Map<String, Input>): Int {
		val inputCopy = input.toMap()
		return inputCopy["a"]!!.getValue(inputCopy)
	}

	override fun part2(input: Map<String, Input>): Int {
		val inputCopy = input.toMutableMap()
		inputCopy["b"] = Signal(inputCopy["a"]!!.getValue(inputCopy))
		inputCopy.values.filterIsInstance<Input.Cacheable>().forEach { it.clearCache() }
		return inputCopy["a"]!!.getValue(inputCopy)
	}

	class Tests {
		private val task = Day7()

		private val rawInput = """
			123 -> x
			456 -> y
			x AND y -> d
			x OR y -> e
			x LSHIFT 2 -> f
			y RSHIFT 2 -> g
			NOT x -> h
			NOT y -> i
		""".trimIndent()

		@TestFactory
		fun parse(): Collection<DynamicTest> = createTestCases(
				"d" expects 72,
				"e" expects 507,
				"f" expects 492,
				"g" expects 114,
				"h" expects 65412,
				"i" expects 65079,
				"x" expects 123,
				"y" expects 456
		) { entry, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, input[entry]!!.getValue(input))
		}
	}
}