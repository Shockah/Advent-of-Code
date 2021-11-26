package pl.shockah.aoc.y2016

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.createRawPart1TestCases
import pl.shockah.aoc.expects
import pl.shockah.aoc.parse3
import java.util.regex.Pattern

class Day4: AdventTask<List<Day4.Entry>, Int, Int>(2016, 4) {
	private val inputPattern = Pattern.compile("([a-z\\-]+)-(\\d+)\\[([a-z]+)]")

	data class Entry(
		val name: List<String>,
		val sectorId: Int,
		val checksum: String
	) {
		val isReal: Boolean
			get() {
				val occurences = name.flatMap { it.toCharArray().asIterable() }.groupingBy { it }.eachCount().toList().sortedWith(Comparator { o1, o2 ->
					if (o1.second != o2.second)
						return@Comparator -o1.second.compareTo(o2.second)
					return@Comparator o1.first.compareTo(o2.first)
				})
				checksum.forEachIndexed { index, c ->
					if (occurences[index].first != c)
						return false
				}
				return true
			}

		val realName: List<String> by lazy {
			return@lazy name.map { it.toCharArray().map { c -> 'a' + ((c - 'a') + sectorId) % ('z' - 'a' + 1) }.joinToString("") }
		}
	}

	override fun parseInput(rawInput: String): List<Entry> {
		return rawInput.lines().map {
			val (name, sectorId, checksum) = inputPattern.parse3<String, Int, String>(it)
			return@map Entry(name.split("-"), sectorId, checksum)
		}
	}

	override fun part1(input: List<Entry>): Int {
		return input.filter { it.isReal }.sumOf { it.sectorId }
	}

	override fun part2(input: List<Entry>): Int {
		return input.filter { it.isReal }.first {
			val realName = it.realName.joinToString(" ")
			return@first realName.contains("north") && realName.contains("pole") && realName.contains("object")
		}.sectorId
	}

	class Tests {
		private val task = Day4()

		@TestFactory
		fun part1(): Collection<DynamicTest> = task.createRawPart1TestCases(
			"aaaaa-bbb-z-y-x-123[abxyz]" expects 123,
			"a-b-c-d-e-f-g-h-987[abcde]" expects 987,
			"not-a-real-room-404[oarel]" expects 404,
			"totally-real-room-200[decoy]" expects 0
		)

		@Test
		fun part2() {
			val input = task.parseInput("qzmt-zixmtkozy-ivhz-343[a]")
			Assertions.assertEquals("very encrypted name", input.first().realName.joinToString(" "))
		}
	}
}