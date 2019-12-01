package pl.shockah.aoc.y2017

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.collection.maxIndex

class Day6: AdventTask<IntArray, Int, Int>(2017, 6) {
	override fun parseInput(rawInput: String): IntArray {
		return rawInput.split(inputSplitPattern).map { it.toInt() }.toIntArray()
	}

	private enum class Mode {
		Cycles, LoopLength
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun task(input: IntArray, mode: Mode): Int {
		val blocks = input.copyOf()
		val configurations = mutableListOf(blocks.toList())
		var cycles = 0

		while (true) {
			val redistributedIndex = blocks.maxIndex()!!
			val toRedistribute = blocks[redistributedIndex]
			blocks[redistributedIndex] = 0
			for (i in 0 until toRedistribute) {
				blocks[(redistributedIndex + i + 1) % blocks.size]++
			}

			cycles++
			val configuration = blocks.toList()

			when (mode) {
				Mode.Cycles -> {
					if (configuration in configurations)
						return cycles
				}
				Mode.LoopLength -> {
					val configurationIndex = configurations.indexOf(configuration)
					if (configurationIndex != -1)
						return configurations.size - configurationIndex
				}
			}
			configurations += configuration
		}
	}

	override fun part1(input: IntArray): Int {
		return task(input, Mode.Cycles)
	}

	override fun part2(input: IntArray): Int {
		return task(input, Mode.LoopLength)
	}

	class Tests {
		private val task = Day6()

		@Test
		fun part1() {
			val input = task.parseInput("0 2 7 0")
			Assertions.assertEquals(5, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput("0 2 7 0")
			Assertions.assertEquals(4, task.part2(input))
		}
	}
}