package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.aoc.parse2
import pl.shockah.unikorn.collection.MutableCircularListImpl
import java.util.regex.Pattern

class Day9: AdventTask<Day9.Input, Long, Long>(2018, 9) {
	private val inputPattern: Pattern = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points")

	data class Input(
			val playerCount: Int,
			val lastMarble: Int
	)

	data class Marble(
			val value: Int
	) {
		var clockwise: Marble = this
		var counterClockwise: Marble = this
	}

	override fun parseInput(rawInput: String): Input {
		val (playerCount, lastMarble) = inputPattern.parse2<Int, Int>(rawInput)
		return Input(playerCount, lastMarble)
	}

	override fun part1(input: Input): Long {
		val players = LongArray(input.playerCount)
		val marbles = MutableCircularListImpl<Int>().also { it.add(0) }
		var nextMarbleToPush = 1

		while (nextMarbleToPush <= input.lastMarble) {
			for (i in players.indices) {
				if (nextMarbleToPush > input.lastMarble)
					break

				if (nextMarbleToPush % 23 == 0) {
					players[i] += nextMarbleToPush.toLong()
					marbles.rotate(-6)
					players[i] += marbles.removePrevious().toLong()
				} else {
					marbles.rotate(1)
					marbles.addNext(nextMarbleToPush)
					marbles.rotate(1)
				}
				nextMarbleToPush++
			}
		}
		return players.maxOrNull()!!
	}

	override fun part2(input: Input): Long {
		return part1(Input(input.playerCount, input.lastMarble * 100))
	}

	class Tests {
		private val task = Day9()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				"9 players; last marble is worth 25 points" expects 32L,
				"10 players; last marble is worth 1618 points" expects 8317L,
				"13 players; last marble is worth 7999 points" expects 146373L,
				"17 players; last marble is worth 1104 points" expects 2764L,
				"21 players; last marble is worth 6111 points" expects 54718L,
				"30 players; last marble is worth 5807 points" expects 37305L
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}
	}
}