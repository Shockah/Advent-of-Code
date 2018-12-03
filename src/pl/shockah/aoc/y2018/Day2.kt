package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day2: AdventTask<List<String>, Int, String>(2018, 2) {
	override fun parseInput(rawInput: String): List<String> {
		return rawInput.lines()
	}

	override fun part1(input: List<String>): Int {
		var hasTwo = 0
		var hasThree = 0

		for (word in input) {
			val groups = word.toCharArray().groupBy { it }.values.filter { it.size in 2..3 }.sortedByDescending { it.size }
			if (groups.any { it.size == 2 })
				hasTwo++
			if (groups.any { it.size == 3 })
				hasThree++
		}

		return hasTwo * hasThree
	}

	override fun part2(input: List<String>): String {
		for (i in 0 until input.size) {
			val id1 = input[i]
			outer@ for (j in (i + 1) until input.size) {
				val id2 = input[j]
				if (id1.length != id2.length)
					throw IllegalArgumentException()

				var differenceIndex: Int? = null
				for (k in 0 until id1.length) {
					if (id1[k] != id2[k]) {
						if (differenceIndex != null)
							continue@outer
						differenceIndex = k
					}
				}

				differenceIndex?.let {
					return StringBuilder(id1).deleteCharAt(it).toString()
				}
			}
		}
		throw IllegalArgumentException("No matching pairs.")
	}

	class Tests {
		private val task = Day2()

		@Test
		fun part1() {
			val input = task.parseInput("""
					abcdef
					bababc
					abbcde
					abcccd
					aabcdd
					abcdee
					ababab
				""".trimIndent())
			Assertions.assertEquals(12, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput("""
					abcde
					fghij
					klmno
					pqrst
					fguij
					axcye
					wvxyz
				""".trimIndent())
			Assertions.assertEquals("fgij", task.part2(input))
		}
	}
}