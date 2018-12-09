package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.aoc.parse2
import java.util.regex.Pattern

class Day9 : AdventTask<Day9.Input, Long, Long>(2018, 9) {
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

	class MarbleBoard {
		private var current: Marble = Marble(0)
		var nextMarbleToPush: Int = 1

		fun rotateClockwise() {
			current = current.clockwise
		}

		fun rotateCounterClockwise() {
			current = current.counterClockwise
		}

		fun push(): Int {
			if (nextMarbleToPush % 23 == 0) {
				var score = nextMarbleToPush++
				repeat(6) {
					rotateCounterClockwise()
				}
				score += removeCounterClockwise()
				return score
			} else {
				rotateClockwise()
				pushClockwise()
				rotateClockwise()
				return 0
			}
		}

		private fun pushClockwise() {
			val marble = Marble(nextMarbleToPush++)
			val tmp = current.clockwise
			current.clockwise = marble
			marble.counterClockwise = current
			marble.clockwise = tmp
			tmp.counterClockwise = marble
		}

		private fun pushCounterClockwise() {
			val marble = Marble(nextMarbleToPush++)
			val tmp = current.counterClockwise
			current.counterClockwise = marble
			marble.clockwise = current
			marble.counterClockwise = tmp
			tmp.clockwise = marble
		}

		private fun removeCounterClockwise(): Int {
			val removed = current.counterClockwise.value
			current.counterClockwise.counterClockwise.clockwise = current
			current.counterClockwise = current.counterClockwise.counterClockwise
			return removed
		}
	}

	override fun parseInput(rawInput: String): Input {
		val (playerCount, lastMarble) = inputPattern.parse2<Int, Int>(rawInput)
		return Input(playerCount, lastMarble)
	}

	override fun part1(input: Input): Long {
		val players = LongArray(input.playerCount)
		val board = MarbleBoard()
		while (board.nextMarbleToPush <= input.lastMarble) {
			for (i in 0 until players.size) {
				if (board.nextMarbleToPush > input.lastMarble)
					break
				players[i] += board.push().toLong()
			}
		}
		return players.max()!!
	}

	override fun part2(input: Input): Long {
		return part1(Input(input.playerCount, input.lastMarble * 100))
	}

	class Tests {
		private val task = Day9()

		@TestFactory
		fun part1(): Collection<DynamicTest> = createTestCases(
				"9 players; last marble is worth 25 points" expects 32,
				"10 players; last marble is worth 1618 points" expects 8317,
				"13 players; last marble is worth 7999 points" expects 146373,
				"17 players; last marble is worth 1104 points" expects 2764,
				"21 players; last marble is worth 6111 points" expects 54718,
				"30 players; last marble is worth 5807 points" expects 37305
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input))
		}
	}
}