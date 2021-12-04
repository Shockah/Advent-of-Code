package pl.shockah.aoc.y2021

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.collection.Array2D

class Day4: AdventTask<Day4.Input, Int, Int>(2021, 4) {
	data class Input(
		val rng: List<Int>,
		val boards: List<Array2D<Int>>
	)

	override fun parseInput(rawInput: String): Input {
		var lines = rawInput.trim().lines().toMutableList()
		val rng = lines.removeFirst().split(',').map { it.toInt() }
		val boards = mutableListOf<Array2D<Int>>()

		while (lines.isNotEmpty()) {
			if (lines.first().isEmpty()) {
				lines.removeFirst()
				continue
			}

			val boardLines = lines
				.takeWhile { it.isNotEmpty() }
				.map { it.trim().split(Regex("\\D+")).map { it.toInt() } }
			require(boardLines.map { it.size }.toSet().size == 1)
			require(boardLines.size == boardLines.first().size)
			lines = lines.drop(boardLines.size).toMutableList()
			boards += Array2D(boardLines.first().size, boardLines.size) { x, y -> boardLines[y][x] }
		}
		return Input(rng, boards)
	}

	private fun Array2D<Int?>.isWinningBoard(): Boolean {
		(0 until width).forEach {
			if (getColumn(it).all { it == null })
				return true
			if (getRow(it).all { it == null })
				return true
		}

		// diagonals
//		if ((0 until width).all { this[it, it] == null })
//			return true
//		if ((0 until width).all { this[width - it - 1, it] == null })
//			return true

		return false
	}

	override fun part1(input: Input): Int {
		var boards = input.boards.map { Array2D<Int?>(it.width, it.height) { x, y -> it[x, y] } }
		input.rng.forEach { rng ->
			boards = boards.map { board -> board.map { it -> it.takeUnless { it == rng } } }
			boards.forEach {
				if (it.isWinningBoard())
					return it.toList().filterNotNull().sum() * rng
			}
		}
		throw IllegalStateException("No winning board, no more random numbers.")
	}

	override fun part2(input: Input): Int {
		var boards = input.boards.map { Array2D<Int?>(it.width, it.height) { x, y -> it[x, y] } }
		input.rng.forEach { rng ->
			boards = boards.map { board -> board.map { it -> it.takeUnless { it == rng } } }
			val winningBoards = boards.filter { it.isWinningBoard() }
			if (boards.size - winningBoards.size == 0) {
				val board = winningBoards.first()
				return board.toList().filterNotNull().sum() * rng
			}
			boards -= winningBoards
		}
		throw IllegalStateException("No winning board, no more random numbers.")
	}

	class Tests {
		private val task = Day4()

		private val rawInput = """
			7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
			
			22 13 17 11  0
			 8  2 23  4 24
			21  9 14 16  7
			 6 10  3 18  5
			 1 12 20 15 19
			
			 3 15  0  2 22
			 9 18 13 17  5
			19  8  7 25 23
			20 11 10 24  4
			14 21 16 12  6
			
			14 21 17 24  4
			10 16 15  9 19
			18  8 23 26 20
			22 11 13  6  5
			 2  0 12  3  7
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(4512, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(1924, task.part2(input))
		}
	}
}