package pl.shockah.aoc.y2018

class Day1: Year2018<List<Int>, Int, Int>() {
	override val parsedInput: List<Int> by lazy {
		inputFile.readLines().map { it.toInt() }
	}

	override fun part1(): Int {
		return parsedInput.sum()
	}

	override fun part2(): Int {
		val set = HashSet<Int>()
		var current = 0
		for (change in parsedInput) {
			current += change
			set += current
		}
		while (true) {
			for (change in parsedInput) {
				current += change
				if (current in set)
					return current
				set += current
			}
		}
	}
}