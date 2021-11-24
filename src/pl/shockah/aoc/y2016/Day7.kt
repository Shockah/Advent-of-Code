package pl.shockah.aoc.y2016

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask

class Day7: AdventTask<List<String>, Int, Int>(2016, 7) {
	override fun parseInput(rawInput: String): List<String> {
		return rawInput.trim().lines()
	}

	override fun part1(input: List<String>): Int {
		return input.count {
			var hasAbba = false
			var isInBrackets = false
			for (i in 3 until it.length) {
				when (it[i - 3]) {
					'[' -> isInBrackets = true
					']' -> isInBrackets = false
					else -> {
						if (it[i - 3] == it[i] && it[i - 2] == it[i - 1] && it[i] != it[i - 1]) {
							if (isInBrackets)
								return@count false
							hasAbba = true
						}
					}
				}
			}
			return@count hasAbba
		}
	}

	override fun part2(input: List<String>): Int {
		return input.count {
			val abas = mutableListOf<Pair<Char, Char>>()
			val babs = mutableListOf<Pair<Char, Char>>()
			var isInBrackets = false
			for (i in 2 until it.length) {
				when (it[i - 2]) {
					'[' -> isInBrackets = true
					']' -> isInBrackets = false
					else -> {
						if (it[i - 2] == it[i] && it[i - 1] != it[i]) {
							val pair = Pair(it[i], it[i - 1])
							if (isInBrackets) {
								if (abas.contains(Pair(pair.second, pair.first)))
									return@count true
								babs += pair
							} else {
								if (babs.contains(Pair(pair.second, pair.first)))
									return@count true
								abas += pair
							}
						}
					}
				}
			}
			return@count false
		}
	}

	class Tests {
		private val task = Day7()

		@Test
		fun part1() {
			val input = task.parseInput("""
				abba[mnop]qrst
				abcd[bddb]xyyx
				aaaa[qwer]tyui
				ioxxoj[asdfgh]zxcvbn
			""".trimIndent())
			Assertions.assertEquals(2, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput("""
				aba[bab]xyz
				xyx[xyx]xyx
				aaa[kek]eke
				zazbz[bzb]cdb
			""".trimIndent())
			Assertions.assertEquals(3, task.part2(input))
		}
	}
}