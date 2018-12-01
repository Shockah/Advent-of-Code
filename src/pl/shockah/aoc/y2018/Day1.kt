package pl.shockah.aoc.y2018

import pl.shockah.aoc.AdventTask
import java.io.File

class Day1: AdventTask<List<Int>, Int, Int>(2018, 1) {
	override fun parseInput(file: File): List<Int> {
		return file.readLines().map { it.toInt() }
	}

	override fun part1(input: List<Int>): Int {
		return input.sum()
	}

	override fun part2(input: List<Int>): Int {
		val set = mutableSetOf(0)
		var current = 0
		for (change in input) {
			current += change
			set += current
		}
		while (true) {
			for (change in input) {
				current += change
				if (current in set)
					return current
				set += current
			}
		}
	}
}