package pl.shockah.aoc.y2017

import pl.shockah.aoc.AdventTask
import java.io.File
import java.util.regex.Pattern

class Day4: AdventTask<List<List<String>>, Int, Int>(2017, 4) {
	private val inputSplitPattern: Pattern = Pattern.compile("\\s+")

	override fun parseInput(file: File): List<List<String>> {
		return file.readLines().map {
			it.trim().split(inputSplitPattern)
		}
	}

	override fun part1(input: List<List<String>>): Int {
		return input.filter {
			setOf(*it.toTypedArray()).size == it.size
		}.size
	}

	override fun part2(input: List<List<String>>): Int {
		return input.filter {
			setOf(*it.map {
				it.toCharArray().sortedArray().contentToString()
			}.toTypedArray()).size == it.size
		}.size
	}
}