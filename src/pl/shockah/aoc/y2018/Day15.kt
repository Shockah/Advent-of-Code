package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.Array2D
import pl.shockah.aoc.MutableArray2D
import pl.shockah.aoc.expects
import java.util.*
import kotlin.Comparator

private const val elfSymbol = 'E'
private const val goblinSymbol = 'G'
private const val wallSymbol = '#'
private const val emptySymbol = '.'

private val printGrids = PrintGrids.InitialAndFinal

private enum class PrintGrids {
	Never, InitialAndFinal, Always
}

private operator fun <T> Array2D<T>.get(vector: Day15.Vector): T {
	return this[vector.x, vector.y]
}

private operator fun <T> MutableArray2D<T>.set(vector: Day15.Vector, value: T) {
	this[vector.x, vector.y] = value
}

class Day15: AdventTask<Day15.Input, Int, Int>(2018, 15) {
	data class Input(
			val grid: Array2D<Entity>,
			val units: List<Entity.Unit>
	)

	data class Vector(
			val x: Int,
			val y: Int
	) {
		val neighbors: Array<Vector>
			get() = arrayOf(Vector(x - 1, y), Vector(x + 1, y), Vector(x, y - 1), Vector(x, y + 1))

		fun getXY(grid: Array2D<Entity>): Int {
			return y * grid.width + x
		}

		operator fun plus(vector: Vector): Vector {
			return Vector(x + vector.x, y + vector.y)
		}

		operator fun minus(vector: Vector): Vector {
			return Vector(x - vector.x, y - vector.y)
		}
	}

	sealed class Entity(
			val symbol: Char
	) {
		abstract class Unit(
				symbol: Char,
				var position: Vector
		) : Entity(symbol) {
			var health: Int = 200

			val isDead: Boolean
				get() = health <= 0

			val isAlive: Boolean
				get() = !isDead

			fun advance(grid: MutableArray2D<Entity>, units: List<Unit>) {
				val aliveEnemies = units.filter { it.isAlive && isEnemy(it) }
				if (aliveEnemies.isEmpty())
					return

				if (position in aliveEnemies.flatMap { it.position.neighbors.toList() }) {
					attack(grid)
				} else {
					move(grid, aliveEnemies)
					if (position in aliveEnemies.flatMap { it.position.neighbors.toList() })
						attack(grid)
				}
			}

			abstract fun isEnemy(unit: Unit): Boolean

			private fun pathToClosestPoint(grid: Array2D<Entity>, to: Set<Vector>): List<Vector>? {
				data class Entry(
						val position: Vector,
						val path: List<Vector>
				) {
					val distance: Int
						get() = path.size
				}

				val toCheck = LinkedList<Entry>()
				toCheck += Entry(position, listOf())
				val checkedPositions = mutableSetOf<Vector>()
				val shortest = mutableMapOf<Vector, Entry>()

				while (!toCheck.isEmpty()) {
					val entry = toCheck.removeFirst()
					checkedPositions += entry.position

					if (grid[entry.position] != Empty && entry.position != position)
						continue

					val newDistance = entry.distance + 1

					val currentShortest = shortest[entry.position]
					if (currentShortest == null || currentShortest.distance > newDistance || (newDistance > 0 && currentShortest.distance == newDistance && currentShortest.path[0].getXY(grid) > entry.path[0].getXY(grid))) {
						shortest[entry.position] = entry
						toCheck += entry.position.neighbors.filter { it !in checkedPositions }.map { Entry(it, entry.path + it) }
					}
				}

				return to.mapNotNull { shortest[it] }.sortedWith(Comparator { o1, o2 ->
					return@Comparator if (o1.distance != o2.distance)
						Integer.compare(o1.distance, o2.distance)
					else
						Integer.compare(o1.position.getXY(grid), o2.position.getXY(grid))
				}).map { it.path }.firstOrNull()
			}

			private fun move(grid: MutableArray2D<Entity>, enemies: List<Unit>) {
				val pathToFollow = pathToClosestPoint(grid, enemies.flatMap { it.position.neighbors.filter { grid[it] == Empty }.toList() }.toSet()) ?: return
				if (!pathToFollow.isEmpty()) {
					grid[position] = Empty
					position = pathToFollow[0]
					grid[position] = this
				}
			}

			private fun attack(grid: MutableArray2D<Entity>) {
				val attackableUnits = position.neighbors.filter { canAttack(grid, it) }.map { grid[it] }.filterIsInstance<Unit>()
				attackableUnits.sortedWith(Comparator { o1, o2 ->
					return@Comparator if (o1.health != o2.health)
						Integer.compare(o1.health, o2.health)
					else
						Integer.compare(o1.position.getXY(grid), o2.position.getXY(grid))
				}).firstOrNull()?.let { attack(grid, it) }
			}

			private fun canAttack(grid: MutableArray2D<Entity>, vector: Vector): Boolean {
				val entity = grid[vector]
				return entity is Unit && isEnemy(entity)
			}

			private fun attack(grid: MutableArray2D<Entity>, enemy: Unit) {
				enemy.health -= 3
				if (enemy.isDead)
					grid[enemy.position] = Empty
			}
		}

		class Elf(
				position: Vector
		) : Unit(elfSymbol, position) {
			override fun isEnemy(unit: Unit): Boolean {
				return unit is Goblin
			}
		}

		class Goblin(
				position: Vector
		) : Unit(goblinSymbol, position) {
			override fun isEnemy(unit: Unit): Boolean {
				return unit is Elf
			}
		}

		object Wall : Entity(wallSymbol)

		object Empty : Entity(emptySymbol)
	}

	override fun parseInput(rawInput: String): Input {
		val lines = rawInput.lines()

		val units = mutableListOf<Entity.Unit>()

		val grid = Array2D(lines.map { it.length }.max()!!, lines.size) { x, y ->
			val position = Vector(x, y)
			when (lines[y][x]) {
				elfSymbol -> {
					val entity = Entity.Elf(position)
					units += entity
					return@Array2D entity
				}
				goblinSymbol -> {
					val entity = Entity.Goblin(position)
					units += entity
					return@Array2D entity
				}
				emptySymbol -> return@Array2D Entity.Empty
				wallSymbol -> return@Array2D Entity.Wall
				else -> throw IllegalArgumentException()
			}
		}

		return Input(grid, units)
	}

	private enum class ResultType {
		FullTurns, Outcome
	}

	private fun part1(input: Input, resultType: ResultType): Int {
		fun println(grid: Array2D<Entity>) {
			println((0 until grid.height).joinToString("\n") { y ->
				val units = mutableListOf<Entity.Unit>()
				val gridString = (0 until grid.width).map { x ->
					val entity = grid[x, y]
					if (entity is Entity.Unit)
						units += entity
					return@map entity.symbol
				}.joinToString("")
				return@joinToString "$gridString  ${units.joinToString(", ") { "${it.symbol}(${it.health})" }}"
			})
		}

		val units = mutableListOf<Entity.Unit>()
		val grid = MutableArray2D(input.grid.width, input.grid.height) { x, y ->
			val entity = input.grid[x, y]
			when (entity) {
				is Entity.Elf -> {
					val newEntity = Entity.Elf(entity.position)
					units += newEntity
					return@MutableArray2D newEntity
				}
				is Entity.Goblin -> {
					val newEntity = Entity.Goblin(entity.position)
					units += newEntity
					return@MutableArray2D newEntity
				}
				else -> entity
			}
		}

		println("> Turn 0")
		if (printGrids != PrintGrids.Never)
			println(grid)

		var turns = 0
		while (true) {
			val toMove = LinkedList(units.filter { it.isAlive })

			while (!toMove.isEmpty()) {
				val unit = toMove.sortedBy { it.position.getXY(grid) }.first()
				toMove -= unit

				if (unit.isDead)
					continue

				val aliveUnits = units.filter { it.isAlive }
				val aliveElves = aliveUnits.filterIsInstance<Entity.Elf>()
				val aliveGoblins = aliveUnits.filterIsInstance<Entity.Goblin>()

				if (aliveElves.isEmpty() || aliveGoblins.isEmpty()) {
					println("> Last turn: ${turns + 1}")
					if (printGrids != PrintGrids.Never)
						println(grid)

					return when (resultType) {
						ResultType.Outcome -> turns * aliveUnits.map { it.health }.sum()
						ResultType.FullTurns -> turns
					}
				}

				unit.advance(grid, units)
			}

			println("> Turn ${turns + 1}")
			if (printGrids == PrintGrids.Always)
				println(grid)

			turns++
		}
	}

	override fun part1(input: Input): Int {
		return part1(input, ResultType.Outcome)
	}

	override fun part2(input: Input): Int {
		TODO()
	}

	class Tests {
		private val task = Day15()

		@TestFactory
		fun part1Outcome(): Collection<DynamicTest> = createTestCases(
				"""
					#######
					#.G...#
					#...EG#
					#.#.#G#
					#..G#E#
					#.....#
					#######
				""".trimIndent() expects 27730,
				"""
					################
					#.......G......#
					#G.............#
					#..............#
					#....###########
					#....###########
					#.......EG.....#
					################
				""".trimIndent() expects 18468
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input, ResultType.Outcome))
		}

		@TestFactory
		fun part1FullTurns(): Collection<DynamicTest> = createTestCases(
				"""
					####
					##E#
					#GG#
					####
				""".trimIndent() expects 67,
				"""
					#####
					#GG##
					#.###
					#..E#
					#.#G#
					#.E##
					#####
				""".trimIndent() expects 71,
				"""
					#######
					#.E..G#
					#.#####
					#G#####
					#######
				""".trimIndent() expects 34
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(expected, task.part1(input, ResultType.FullTurns))
		}
	}
}