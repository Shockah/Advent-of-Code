package pl.shockah.aoc.y2019

import pl.shockah.aoc.AdventTask

class Day4: AdventTask<IntRange, Int, Int>(2019, 4) {
	override fun parseInput(rawInput: String): IntRange {
		val split = rawInput.split("-")
		return split[0].toInt()..split[1].toInt()
	}

	private fun <T> Iterable<T>.groupDuplicates(): List<Pair<T, Int>> {
		var last: T? = null
		var count = 0
		val results = mutableListOf<Pair<T, Int>>()

		for (element in this) {
			when {
				count == 0 -> count++
				last == element -> count++
				else -> {
					results += last!! to count
					count = 1
				}
			}
			last = element
		}

		results += last!! to count
		return results
	}

	private fun isValid(password: String, range: IntRange, allowsTripleDigit: Boolean): Boolean {
		fun containsDoubleDigit(): Boolean {
			val results = password.toList().groupDuplicates()
			if (allowsTripleDigit)
				return results.any { it.second >= 2 }
			else
				return results.any { it.second == 2 }
		}

		fun isInRange(): Boolean {
			return password.toInt() in range
		}

		return containsDoubleDigit() && isInRange()
	}

	private fun buildPasswords(current: String, digitsLeft: Int): List<String> {
		fun getAllowedDigits(password: String): CharRange {
			return if (password.isEmpty()) '0'..'9' else password.last()..'9'
		}

		if (digitsLeft == 0)
			return listOf(current)
		return getAllowedDigits(current).flatMap { buildPasswords(current + it, digitsLeft - 1) }
	}

	private fun task(input: IntRange, allowsTripleDigit: Boolean): Int {
		return buildPasswords("", 6).filter { isValid(it, input, allowsTripleDigit) }.size
	}

	override fun part1(input: IntRange): Int {
		return task(input, true)
	}

	override fun part2(input: IntRange): Int {
		return task(input, false)
	}
}