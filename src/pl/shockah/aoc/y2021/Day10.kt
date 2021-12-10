package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.Ref

class Day10: AdventTask<List<String>, Int, Long>(2021, 10) {
	companion object {
		private val chunkTypes: Map<Pair<Char, Char>, ChunkTypeScore> = listOf(
			('(' to ')') to ChunkTypeScore(3, 1),
			('[' to ']') to ChunkTypeScore(57, 2),
			('{' to '}') to ChunkTypeScore(1197, 3),
			('<' to '>') to ChunkTypeScore(25137, 4)
		).toMap()

		private const val repairMultiplier = 5
	}

	override fun parseInput(rawInput: String): List<String> {
		return rawInput.trim().lines()
	}

	private data class ChunkTypeScore(
		val fail: Int,
		val repair: Int
	)

	private sealed class ParseResult {
		data class Chunk(
			val type: Pair<Char, Char>,
			val body: List<Chunk>
		): ParseResult() {
			override fun toString(): String {
				return "${type.first}${body.joinToString("") { "$it" }}${type.second}"
			}
		}

		data class Incomplete(
			val string: String
		): ParseResult()

		data class Failure(
			val expected: Pair<Char, Char>,
			val found: Pair<Char, Char>
		): ParseResult()
	}

	private fun parseChunk(line: String, pointer: Ref<Int>, repair: Ref<List<Pair<Char, Char>>>?): ParseResult {
		var chunkType: Pair<Char, Char>? = null
		val firstC = line[pointer.value++]
		for ((begin, end) in chunkTypes.keys) {
			if (firstC == begin) {
				chunkType = begin to end
				break
			} else if (firstC == end) {
				return ParseResult.Incomplete(line)
			}
		}
		if (chunkType == null)
			throw IllegalArgumentException()

		val body = mutableListOf<ParseResult.Chunk>()
		outerLoop@ while (pointer.value < line.length) {
			val c = line[pointer.value]
			for ((begin, end) in chunkTypes.keys) {
				if (c == begin) {
					when (val result = parseChunk(line, pointer, repair)) {
						is ParseResult.Chunk -> body += result
						else -> return result
					}
					continue@outerLoop
				} else if (c == end) {
					if (c == chunkType.second) {
						pointer.value++
						return ParseResult.Chunk(chunkType, body)
					} else {
						return ParseResult.Failure(chunkType, begin to end)
					}
				}
			}
			throw IllegalArgumentException()
		}
		if (repair == null) {
			return ParseResult.Incomplete(line)
		} else {
			repair.value += chunkType
			return ParseResult.Chunk(chunkType, body)
		}
	}

	private fun parseChunk(line: String, repair: Ref<List<Pair<Char, Char>>>?): ParseResult {
		val pointer = Ref(0)
		return parseChunk(line, pointer, repair)
	}

	override fun part1(input: List<String>): Int {
		val parsed = input.map { parseChunk(it, null) }
		return parsed.sumOf {
			return@sumOf when (it) {
				is ParseResult.Failure -> chunkTypes[it.found]!!.fail
				else -> 0
			}
		}
	}

	override fun part2(input: List<String>): Long {
		val repairScores = input.mapNotNull {
			val repair = Ref<List<Pair<Char, Char>>>(emptyList())
			when (parseChunk(it, repair)) {
				is ParseResult.Chunk -> return@mapNotNull repair.value.fold(0L) { acc, chunkType -> acc * repairMultiplier + chunkTypes[chunkType]!!.repair }
				else -> return@mapNotNull null
			}
		}.filter { it != 0L }.sorted()
		return repairScores[repairScores.size / 2]
	}

	class Tests {
		private val task = Day10()

		private val rawInput = """
			[({(<(())[]>[[{[]{<()<>>
			[(()[<>])]({[<{<<[]>>(
			{([(<{}[<>[]}>{[]{[(<()>
			(((({<>}<{<{<>}{[]{[]{}
			[[<[([]))<([[{}[[()]]]
			[{[{({}]{}}([{[{{{}}([]
			{<[[]]>}<{[{[{[]{()[[[]
			[<(<(<(<{}))><([]([]()
			<{([([[(<>()){}]>(<<{{
			<{([{{}}[<[[[<>{}]]]>[]]
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(26397, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(288957, task.part2(input))
		}
	}
}