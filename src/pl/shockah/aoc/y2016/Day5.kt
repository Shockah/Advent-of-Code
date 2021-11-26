package pl.shockah.aoc.y2016

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.createRawPart1TestCases
import pl.shockah.aoc.createRawPart2TestCases
import pl.shockah.aoc.expects
import pl.shockah.unikorn.Md5
import kotlin.experimental.and

class Day5: AdventTask<String, String, String>(2016, 5) {
	override fun parseInput(rawInput: String): String {
		return rawInput.trim()
	}

	override fun part1(input: String): String {
		return generateSequence(0) { it + 1 }
			.map { Md5.of("$input$it") }
			.filter { it.bytes[0] == 0x00.toByte() && it.bytes[1] == 0x00.toByte() && it.bytes[2] and 0xF0.toByte() == 0x00.toByte() }
			.map { (it.bytes[2] and 0x0F.toByte()).toString(16) }
			.take(8)
			.joinToString("") { it }
	}

	override fun part2(input: String): String {
		val result = Array<Char?>(8) { null }
		for (i in generateSequence(0) { it + 1 }) {
			val md5Bytes = Md5.of("$input$i")
			if (md5Bytes.bytes[0] != 0x00.toByte() || md5Bytes.bytes[1] != 0x00.toByte() || md5Bytes.bytes[2] and 0xF0.toByte() != 0x00.toByte())
				continue
			val md5 = md5Bytes.bytes.joinToString("") { "%02x".format(it) }

			val position = md5[5] - '0'
			if (position >= 8)
				continue

			if (result[position] != null)
				continue

			result[position] = md5[6]
			println("Hash: $md5")
			if (result.all { it != null })
				break
		}
		return result.map { it!! }.toCharArray().let { String(it) }
	}

	class Tests {
		private val task = Day5()

		@TestFactory
		fun part1(): Collection<DynamicTest> = task.createRawPart1TestCases(
			"abc" expects "18f47a30"
		)

		@TestFactory
		fun part2(): Collection<DynamicTest> = task.createRawPart2TestCases(
			"abc" expects "05ace8e3"
		)
	}
}