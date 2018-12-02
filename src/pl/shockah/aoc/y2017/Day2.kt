package pl.shockah.aoc.y2017

import pl.shockah.aoc.AdventTask

class Day2: AdventTask<List<List<Int>>, Int, Int>(2017, 2) {
	override fun parseInput(rawInput: String): List<List<Int>> {
		return rawInput.lines().map {
			it.trim().split(inputSplitPattern).map { it.toInt() }
		}
	}

	override fun part1(input: List<List<Int>>): Int {
		return input.map { it.max()!! - it.min()!! }.sum()
	}

	override fun part2(input: List<List<Int>>): Int {
		return input.map { it.sorted() }.map {
			var divided = 0
			for (i in 0 until it.size) {
				for (j in i until it.size) {
					if (it[j] != it[i] && it[j] % it[i] == 0) {
						if (divided != 0)
							throw IllegalArgumentException("Invalid input data (extra matching values).")
						divided = it[j] / it[i]
					}
				}
				if (divided != 0)
					return@map divided
			}
			throw IllegalArgumentException("Invalid input data (no matching values).")
		}.sum()
	}
}