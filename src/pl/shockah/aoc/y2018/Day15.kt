package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.unikorn.collection.Array2D
import pl.shockah.unikorn.collection.MutableArray2D
import java.util.*

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

	class AStar(
			val grid: Array2D<Entity>,
			val initialPoint: Vector,
			endPoints: Set<Vector>
	) {
		val endPoints = endPoints.filter { grid[it] == Entity.Empty }
		private val mutablePaths = MutableArray2D<List<Vector>?>(grid.width, grid.height)

		val paths: Array2D<List<Vector>?>
			get() = mutablePaths

		fun execute(): AStar {
			if (endPoints.isEmpty())
				return this

			val evaluated = mutableSetOf<Vector>()
			val toEvaluate = LinkedList<Vector>()
			toEvaluate += initialPoint
			mutablePaths[initialPoint] = listOf()
			var reachedLength: Int? = null

			while (!toEvaluate.isEmpty()) {
				val current = toEvaluate.removeFirst()
				val currentPath = mutablePaths[current]!!
				evaluated += current

				if (reachedLength != null && reachedLength < currentPath.size)
					continue
				if (current in endPoints) {
					if (reachedLength == null)
						reachedLength = currentPath.size
				}

				if (reachedLength == null) {
					for (neighbor in current.neighbors) {
						if (grid[neighbor] != Entity.Empty)
							continue
						if (neighbor in evaluated)
							continue

						val existingPath = mutablePaths[neighbor]
						val newPathLength = currentPath.size + 1
						if (existingPath == null || newPathLength < existingPath.size || (newPathLength == existingPath.size && currentPath[0].getXY(grid) < existingPath[0].getXY(grid)))
							mutablePaths[neighbor] = currentPath + neighbor

						toEvaluate += neighbor
					}
				}
			}

			return this
		}
	}

	sealed class Entity(
			val symbol: Char
	) {
		abstract class Unit(
				symbol: Char,
				var position: Vector
		): Entity(symbol) {
			var health: Int = 200

			val isDead: Boolean
				get() = health <= 0

			val isAlive: Boolean
				get() = !isDead

			fun advance(grid: MutableArray2D<Entity>, units: List<Unit>, elfPower: Int = 3) {
				val aliveEnemies = units.filter { it.isAlive && isEnemy(it) }
				if (aliveEnemies.isEmpty())
					return

				if (position in aliveEnemies.flatMap { it.position.neighbors.toList() }) {
					attack(grid, elfPower)
				} else {
					move(grid, aliveEnemies)
					if (position in aliveEnemies.flatMap { it.position.neighbors.toList() })
						attack(grid, elfPower)
				}
			}

			abstract fun isEnemy(unit: Unit): Boolean

			private fun pathToClosestPoint(grid: Array2D<Entity>, to: Set<Vector>): List<Vector>? {
				val astar = AStar(grid, position, to).execute()
				return to.mapNotNull { astar.paths[it] }.sortedWith(compareBy({ it.size }, { it.last().getXY(grid) })).firstOrNull()
			}

			private fun move(grid: MutableArray2D<Entity>, enemies: List<Unit>) {
				val pathToFollow = pathToClosestPoint(grid, enemies.flatMap { it.position.neighbors.filter { grid[it] == Empty }.toList() }.toSet()) ?: return
				if (pathToFollow.isNotEmpty()) {
					grid[position] = Empty
					position = pathToFollow[0]
					grid[position] = this
				}
			}

			private fun attack(grid: MutableArray2D<Entity>, elfPower: Int) {
				val attackableUnits = position.neighbors.filter { canAttack(grid, it) }.map { grid[it] }.filterIsInstance<Unit>()
				attackableUnits.sortedWith(compareBy(Unit::health, { it.position.getXY(grid) })).firstOrNull()?.let { attack(grid, it, elfPower) }
			}

			private fun canAttack(grid: MutableArray2D<Entity>, vector: Vector): Boolean {
				val entity = grid[vector]
				return entity is Unit && isEnemy(entity)
			}

			private fun attack(grid: MutableArray2D<Entity>, enemy: Unit, elfPower: Int) {
				enemy.health -= if (this is Elf) elfPower else 3
				if (enemy.isDead)
					grid[enemy.position] = Empty
			}
		}

		class Elf(
				position: Vector
		): Unit(elfSymbol, position) {
			override fun isEnemy(unit: Unit): Boolean {
				return unit is Goblin
			}
		}

		class Goblin(
				position: Vector
		): Unit(goblinSymbol, position) {
			override fun isEnemy(unit: Unit): Boolean {
				return unit is Elf
			}
		}

		object Wall: Entity(wallSymbol)

		object Empty: Entity(emptySymbol)
	}

	override fun parseInput(rawInput: String): Input {
		val lines = rawInput.lines()

		val units = mutableListOf<Entity.Unit>()

		val grid = Array2D(lines.maxOf { it.length }, lines.size) { x, y ->
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

	private fun task(input: Input, resultType: ResultType, elfPower: Int = 3, stopAtElfDeath: Boolean = false): Int {
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
			when (val entity = input.grid[x, y]) {
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
			for (unit in units.filter { it.isAlive }.sortedBy { it.position.getXY(grid) }) {
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
						ResultType.Outcome -> turns * aliveUnits.sumOf { it.health }
						ResultType.FullTurns -> turns
					}
				}

				unit.advance(grid, units, elfPower)
				if (stopAtElfDeath && aliveElves.any { it.isDead })
					return 0
			}

			println("> Turn ${turns + 1}")
			if (printGrids == PrintGrids.Always)
				println(grid)

			turns++
		}
	}

	override fun part1(input: Input): Int {
		return task(input, ResultType.Outcome)
	}

	override fun part2(input: Input): Int {
		var power = 4
		while (true) {
			val outcome = task(input, ResultType.Outcome, power, true)
			if (outcome != 0) {
				println("> Required elf attack power: $power")
				return outcome
			}
			power++
		}
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
			Assertions.assertEquals(expected, task.task(input, ResultType.Outcome))
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
			Assertions.assertEquals(expected, task.task(input, ResultType.FullTurns))
		}

		@TestFactory
		fun part1GoblinFirstMove(): Collection<DynamicTest> = createTestCases(
				"""
					#####
					#.#E#
					#.#.#
					#.G.#
					#...#
					#E..#
					#####
				""".trimIndent() expects Vector(3, 3)
		) { rawInput, expected ->
			val input = task.parseInput(rawInput)
			val goblin = input.units.first { it is Entity.Goblin }
			goblin.advance(MutableArray2D(input.grid.width, input.grid.height) { x, y -> input.grid[x, y] }, input.units)
			Assertions.assertEquals(expected, goblin.position)
		}
	}
}