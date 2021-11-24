package pl.shockah.aoc.y2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import java.util.*
import kotlin.math.min

class Day6: AdventTask<Pair<Day6.Celestial, Map<String, Day6.Celestial>>, Int, Int>(2019, 6) {
	class Celestial(
			val identifier: String,
			val root: Celestial? = null
	) {
		val orbitedBy = mutableListOf<Celestial>()

		val rootPathWithoutSelf: List<Celestial>
			get() {
				if (root == null)
					return emptyList()
				return root.rootPathWithoutSelf + root
			}

		val rootPath: List<Celestial>
			get() = rootPathWithoutSelf + this

		fun findCommonCelestial(other: Celestial): Celestial {
			val rootPath1 = rootPath
			val rootPath2 = other.rootPath

			if (rootPath1[0] != rootPath2[0])
				throw IllegalStateException("No common celestial between $this and $other.")

			for (i in 1 until min(rootPath1.size, rootPath2.size)) {
				if (rootPath1[i] != rootPath2[i])
					return rootPath1[i - 1]
			}
			throw IllegalStateException("Can't find a route between $this and $other.")
		}

		fun findPath(other: Celestial): List<Celestial> {
			val common = findCommonCelestial(other)
			val path1 = mutableListOf<Celestial>()
			val path2 = mutableListOf<Celestial>()

			var current = this
			while (current != common) {
				current = current.root!!
				path1 += current
			}

			current = other
			while (current != common) {
				current = current.root!!
				path2 += current
			}

			return path1 + path2.reversed().drop(1) + other
		}

		override fun toString(): String {
			return identifier
		}
	}

	private fun parseInputStep1(rawInput: String): List<Pair<String, String>> {
		return rawInput.lines().map {
			val split = it.split(")")
			return@map split[0] to split[1]
		}
	}

	private fun parseInputStep2(input: List<Pair<String, String>>): Pair<Celestial, Map<String, Celestial>> {
		val celestials = mutableMapOf<String, Celestial>()
		val entries = LinkedList(input)
		celestials["COM"] = Celestial("COM")

		while (!entries.isEmpty()) {
			val oldCount = entries.size

			val iterator = entries.iterator()
			while (iterator.hasNext()) {
				val entry = iterator.next()
				celestials[entry.first]?.let {
					val celestial = Celestial(entry.second, it)
					it.orbitedBy += celestial
					celestials[celestial.identifier] = celestial
					iterator.remove()
				}
			}

			if (oldCount == entries.size)
				throw IllegalStateException("Can't figure out orbits for ${entries.size} entries.")
		}
		return celestials["COM"]!! to celestials
	}

	override fun parseInput(rawInput: String): Pair<Celestial, Map<String, Celestial>> {
		return parseInputStep2(parseInputStep1(rawInput))
	}

	override fun part1(input: Pair<Celestial, Map<String, Celestial>>): Int {
		return input.second.values.sumOf { it.rootPathWithoutSelf.size }
	}

	override fun part2(input: Pair<Celestial, Map<String, Celestial>>): Int {
		return input.second["YOU"]!!.findPath(input.second["SAN"]!!).size - 2
	}

	@Nested
	inner class Tests {
		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				listOf("COM" to "B", "B" to "C", "C" to "D", "D" to "E", "E" to "F", "B" to "G", "G" to "H", "D" to "I", "E" to "J", "J" to "K", "K" to "L") expects 42
		) { preParsedInput, expected -> Assertions.assertEquals(expected, part1(parseInputStep2(preParsedInput))) }

		@TestFactory
		fun part2(): Collection<DynamicTest> = createTestCases(
				listOf("COM" to "B", "B" to "C", "C" to "D", "D" to "E", "E" to "F", "B" to "G", "G" to "H", "D" to "I", "E" to "J", "J" to "K", "K" to "L", "K" to "YOU", "I" to "SAN") expects 4
		) { preParsedInput, expected -> Assertions.assertEquals(expected, part2(parseInputStep2(preParsedInput))) }
	}
}