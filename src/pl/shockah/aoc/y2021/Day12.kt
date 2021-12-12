package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.UnorderedPair

class Day12: AdventTask<Day12.Graph, Int, Int>(2021, 12) {
	data class Graph(
		val start: Cave,
		val end: Cave,
		val caves: Set<Cave>
	)

	data class Cave(
		val name: String
	) {
		var connections: Set<Cave> = emptySet()

		val isBig: Boolean
			get() = name.uppercase() == name
	}

	override fun parseInput(rawInput: String): Graph {
		val edges = rawInput.trim().lines().map {
			val split = it.split('-')
			return@map UnorderedPair(split[0].trim(), split[1].trim())
		}

		val caves = edges.flatMap { listOf(it.first, it.second) }.map { Cave(it) }.associateBy { it.name }
		edges.forEach {
			val cave1 = caves[it.first]!!
			val cave2 = caves[it.second]!!
			cave1.connections += cave2
			cave2.connections += cave1
		}
		return Graph(caves["start"]!!, caves["end"]!!, caves.values.toSet())
	}

	private fun countPossiblePaths(end: Cave, caveAllowedTwice: Cave?, currentPath: List<Cave>): Int {
		val current = currentPath.last()
		if (current == end)
			return if (caveAllowedTwice == null) 1 else if (currentPath.count { it == caveAllowedTwice } == 2) 1 else 0
		val possibleCaves = current.connections.filter { cave ->
			val allowedCount = if (caveAllowedTwice == cave) 2 else 1
			return@filter cave.isBig || currentPath.count { it == cave } <= allowedCount - 1
		}
		return possibleCaves.sumOf { countPossiblePaths(end, caveAllowedTwice, currentPath + it) }
	}

	override fun part1(input: Graph): Int {
		return countPossiblePaths(input.end, null, listOf(input.start))
	}

	override fun part2(input: Graph): Int {
		val noRepeatCount = countPossiblePaths(input.end, null, listOf(input.start))
		val withRepeatCount = input.caves
			.filter { it != input.start && it != input.end && !it.isBig }
			.sumOf { countPossiblePaths(input.end, it, listOf(input.start)) }
		return noRepeatCount + withRepeatCount
	}

	class Tests {
		private val task = Day12()

		private val rawInput = """
			start-A
			start-b
			A-c
			A-b
			b-d
			A-end
			b-end
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(10, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(36, task.part2(input))
		}
	}
}