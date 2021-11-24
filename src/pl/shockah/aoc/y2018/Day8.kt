package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day8: AdventTask<Day8.Node, Int, Int>(2018, 8) {
	data class Node(
			val children: List<Node>,
			val metadata: List<Int>
	) {
		val totalMetadataSum: Int by lazy {
			metadata.sum() + children.sumOf { it.totalMetadataSum }
		}

		val value: Int by lazy {
			if (children.isEmpty()) {
				return@lazy metadata.sum()
			} else {
				return@lazy metadata.sumOf { children.getOrNull(it - 1)?.value ?: 0 }
			}
		}
	}

	override fun parseInput(rawInput: String): Node {
		fun parse(iterator: Iterator<Int>): Node {
			val childNodeCount = iterator.next()
			val metadataCount = iterator.next()

			val children = (0 until childNodeCount).map { parse(iterator) }
			val metadata = (0 until metadataCount).map { iterator.next() }
			return Node(children, metadata)
		}

		return parse(rawInput.split(" ").map { it.toInt() }.iterator())
	}

	override fun part1(input: Node): Int {
		return input.totalMetadataSum
	}

	override fun part2(input: Node): Int {
		return input.value
	}

	class Tests {
		private val task = Day8()

		private val rawInput = """
			2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(138, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(66, task.part2(input))
		}
	}
}