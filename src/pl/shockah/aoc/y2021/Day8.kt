package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import java.util.*

class Day8: AdventTask<List<Day8.Input>, Int, Int>(2021, 8) {
	companion object {
		const val segmentCount = 7

		val digits = listOf(
			BitSet(segmentCount).apply { listOf(0, 1, 2, 4, 5, 6).forEach { set(it) } }, // 0
			BitSet(segmentCount).apply { listOf(2, 5).forEach { set(it) } }, // 1
			BitSet(segmentCount).apply { listOf(0, 2, 3, 4, 6).forEach { set(it) } }, // 2
			BitSet(segmentCount).apply { listOf(0, 2, 3, 5, 6).forEach { set(it) } }, // 3
			BitSet(segmentCount).apply { listOf(1, 2, 3, 5).forEach { set(it) } }, // 4
			BitSet(segmentCount).apply { listOf(0, 1, 3, 5, 6).forEach { set(it) } }, // 5
			BitSet(segmentCount).apply { listOf(0, 1, 3, 4, 5, 6).forEach { set(it) } }, // 6
			BitSet(segmentCount).apply { listOf(0, 2, 5).forEach { set(it) } }, // 7
			BitSet(segmentCount).apply { listOf(0, 1, 2, 3, 4, 5, 6).forEach { set(it) } }, // 8
			BitSet(segmentCount).apply { listOf(0, 1, 2, 3, 5, 6).forEach { set(it) } } // 9
		)
	}

	data class Input(
		val patterns: List<BitSet>,
		val value: List<BitSet>
	)

	override fun parseInput(rawInput: String): List<Input> {
		return rawInput.trim().lines().map {
			val split = it.split('|').map { it.trim() }
			val splitPatterns = split[0].split(' ').map { it.trim() }
			val patterns = splitPatterns.map { pattern -> BitSet(segmentCount).apply { (0 until segmentCount).map { set(it, pattern.contains('a' + it)) } } }
			val splitValue = split[1].split(' ').map { it.trim() }
			val value = splitValue.map { pattern -> BitSet(segmentCount).apply { (0 until segmentCount).map { set(it, pattern.contains('a' + it)) } } }
			return@map Input(patterns, value)
		}
	}

	private fun BitSet.getBitsSet(): Set<Int> {
		return (0 until size()).filter { this[it] }.toSet()
	}

	override fun part1(input: List<Input>): Int {
		val uniqueCardinality = digits.groupBy { it.cardinality() }.filter { it.value.size == 1 }.values.flatten().map { it.cardinality() }
		return input.sumOf { it.value.count { it.cardinality() in uniqueCardinality } }
	}

	override fun part2(input: List<Input>): Int {
		val uniqueCardinality = digits.groupBy { it.cardinality() }.filter { it.value.size == 1 }.values.flatten().map { it.cardinality() }.filter { it < segmentCount }
		val uniqueCardinalityDigits = digits.mapIndexed { index, bitSet -> index to bitSet }.filter { it.second.cardinality() in uniqueCardinality }.toMap().mapKeys { it.value.cardinality() }
		val uniqueSegmentTotals = (0 until segmentCount).groupBy { segment -> digits.count { it[segment] } }.filter { it.value.size == 1 }.mapValues { it.value.single() }.map { it.value to it.key }.toMap()

		return input.sumOf { entry ->
			val segmentPossibilities = mutableMapOf<Int, Set<Int>>().apply {
				(0 until segmentCount).forEach { this[it] = (0 until segmentCount).toSet() }
			}

			// filtering out candidates by finding distinct segment totals digits (1: 2 segments, 4: 4 segments, 7: 3 segments, 8: 7 segments)
			val uniqueCardinalityEntryDigits = entry.patterns.filter { it.cardinality() in uniqueCardinality }.map { it to uniqueCardinalityDigits[it.cardinality()]!! }
			uniqueCardinalityEntryDigits.forEach { (entrySegments, realSegments) ->
				realSegments.getBitsSet().forEach { segment ->
					segmentPossibilities[segment] = segmentPossibilities[segment]!!.intersect(entrySegments.getBitsSet())
				}
			}

			// filtering out candidates by finding distinct occurence totals across the whole pattern (e: 4x, f: 9x, b: 6x)
			uniqueSegmentTotals.forEach { (segment, total) ->
				val entrySegment = (0 until segmentCount).first { entrySegment -> entry.patterns.count { it[entrySegment] } == total }
				segmentPossibilities[segment] = setOf(entrySegment)
			}

			// filtering out candidates which have definite picks
			if (segmentPossibilities.values.any { it.size == 1 }) {
				while (true) {
					val oldCounts = segmentPossibilities.map { it.value.size }
					val ones = segmentPossibilities.filter { it.value.size == 1 }.values.map { it.single() }
					for (segment in segmentPossibilities.keys) {
						val current = segmentPossibilities[segment]!!
						if (current.size == 1 && current.single() in ones)
							continue
						segmentPossibilities[segment] = current - ones
					}
					if (segmentPossibilities.map { it.value.size } == oldCounts)
						break
					if (segmentPossibilities.values.all { it.size == 1 })
						break
				}
			}

			require(segmentPossibilities.all { it.value.size == 1 }) { "Could not figure out all segments." }
			val usedSegments = segmentPossibilities.mapValues { it.value.single() }
			val entryDigits = entry.value.map { entryDigit -> BitSet().apply { (0 until segmentCount).forEach { this[it] = entryDigit[usedSegments[it]!!] } } }
			val mappedDigits = entryDigits.map { digits.indexOf(it) }
			return@sumOf mappedDigits.joinToString("").toInt()
		}
	}

	class Tests {
		private val task = Day8()

		private val rawInput = """
			be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
			edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
			fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
			fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
			aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
			fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
			dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
			bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
			egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
			gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(26, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(61229, task.part2(input))
		}
	}
}