package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.*

class Day13: AdventTask<Day13.Input, String, String>(2018, 13) {
	data class Input(
			val grid: Array2D<Rail?>,
			val carts: List<Cart>
	)

	data class Cart(
			var x: Int,
			var y: Int,
			var direction: Direction
	) {
		var turnsCounter: Int = 0
		var collided: Boolean = false

		fun advance(grid: Array2D<Rail?>) {
			x += direction.x
			y += direction.y
			val rail = grid[x, y]

			when (rail) {
				Rail.Slash -> {
					direction = if (direction == Direction.Up || direction == Direction.Down)
						direction.nextInCycle
					else
						direction.previousInCycle
				}
				Rail.Backslash -> {
					direction = if (direction == Direction.Left || direction == Direction.Right)
						direction.nextInCycle
					else
						direction.previousInCycle
				}
				Rail.Crossroad -> {
					when (turnsCounter++ % 3) {
						0 -> direction = direction.previousInCycle
						2 -> direction = direction.nextInCycle
						else -> { }
					}
				}
				else -> { }
			}
		}
	}

	enum class Direction(
			val x: Int,
			val y: Int,
			val symbol: Char,
			val rail: Rail
	) {
		Left(-1, 0, '<', Rail.Horizontal),
		Up(0, -1, '^', Rail.Vertical),
		Right(1, 0, '>', Rail.Horizontal),
		Down(0, 1, 'v', Rail.Vertical);

		companion object {
			val bySymbol = values().map { it.symbol to it }.toMap()
		}
	}

	enum class Rail(
			val symbol: Char
	) {
		Horizontal('-'), Vertical('|'), Slash('/'), Backslash('\\'), Crossroad('+');

		companion object {
			val bySymbol = values().map { it.symbol to it }.toMap()
		}
	}

	private fun toString(grid: Array2D<Rail?>): String {
		return (0 until grid.height).joinToString("\n") { y ->
			(0 until grid.width).joinToString("") { x ->
				(grid[x, y]?.symbol ?: ' ').toString()
			}
		}
	}

	override fun parseInput(rawInput: String): Day13.Input {
		val lines = rawInput.lines()

		val carts = mutableListOf<Cart>()
		val grid = Array2D(lines.map { it.length }.max()!!, lines.size) { x, y ->
			var c = if (x < lines[y].length) lines[y][x] else ' '
			if (c == ' ')
				return@Array2D null

			val direction = Direction.bySymbol[c]
			if (direction != null) {
				carts += Cart(x, y, direction)
				c = direction.rail.symbol
			}

			return@Array2D Rail.bySymbol[c]!!
		}

		return Input(grid, carts)
	}

	private enum class Mode {
		FirstCollision, LastStanding
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			AdventOfCode.main(arrayOf("2018", "13"))
		}
	}

	private fun task(input: Day13.Input, mode: Mode): String {
		fun advance(grid: Array2D<Rail?>, carts: List<Cart>): Pair<Int, Int>? {
			for (cart in carts.sortedBy { it.y * grid.width + it.x }) {
				if (cart.collided)
					continue
				cart.advance(grid)

				val activeCarts = carts.filter { !it.collided }
				val colliding = activeCarts.firstOrNull { it !== cart && it.x == cart.x && it.y == cart.y }
				if (colliding != null) {
					if (mode == Mode.FirstCollision) {
						return cart.x to cart.y
					} else {
						cart.collided = true
						colliding.collided = true
					}
				}
			}

			if (mode == Mode.LastStanding) {
				val activeCarts = carts.filter { !it.collided }
				if (activeCarts.isEmpty())
					throw IllegalStateException()
				if (activeCarts.size == 1)
					return activeCarts[0].x to activeCarts[0].y
			}

			return null
		}

		val carts = input.carts.toList()
		while (true) {
			val collision = advance(input.grid, carts)
			if (collision != null)
				return "${collision.first},${collision.second}"
		}
	}

	override fun part1(input: Day13.Input): String {
		return task(input, Mode.FirstCollision)
	}

	override fun part2(input: Day13.Input): String {
		return task(input, Mode.LastStanding)
	}

	class Tests {
		private val task = Day13()

		@Test
		fun part1() {
			val input = task.parseInput("""
				/->-\
				|   |  /----\
				| /-+--+-\  |
				| | |  | v  |
				\-+-/  \-+--/
				  \------/
			""".trimIndent())
			Assertions.assertEquals("7,3", task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput("""
				/>-<\
				|   |
				| /<+-\
				| | | v
				\>+</ |
				  |   ^
				  \<->/
			""".trimIndent())
			Assertions.assertEquals("6,4", task.part2(input))
		}
	}
}