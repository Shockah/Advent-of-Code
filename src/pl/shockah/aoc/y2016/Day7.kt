package pl.shockah.aoc.y2016

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.createRawPart1TestCases
import pl.shockah.aoc.createRawPart2TestCases
import pl.shockah.aoc.expects

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

		@TestFactory
		fun part1(): Collection<DynamicTest> = task.createRawPart1TestCases(
			"abba[mnop]qrst" expects 1,
			"abcd[bddb]xyyx" expects 0,
			"aaaa[qwer]tyui" expects 0,
			"ioxxoj[asdfgh]zxcvbn" expects 1
		)

		@TestFactory
		fun part2(): Collection<DynamicTest> = task.createRawPart2TestCases(
			"aba[bab]xyz" expects 1,
			"xyx[xyx]xyx" expects 0,
			"aaa[kek]eke" expects 1,
			"zazbz[bzb]cdb" expects 1
		)
	}
}