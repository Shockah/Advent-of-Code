package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse4
import java.util.regex.Pattern

class Day14 : AdventTask<List<Day14.ReindeerStats>, Int, Int>(2015, 14) {
	private val inputPattern: Pattern = Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.")

	data class ReindeerStats(
			val name: String,
			val speed: Int,
			val flightTime: Int,
			val restTime: Int
	)

	class Reindeer(
			val stats: ReindeerStats
	) {
		var timer: Int = 0
		var flying: Boolean = true
		var distance: Int = 0
		var score: Int = 0

		fun tick() {
			if (flying)
				distance += stats.speed
			timer++

			val stateChangeTime = if (flying) stats.flightTime else stats.restTime
			if (timer == stateChangeTime) {
				flying = !flying
				timer = 0
			}
		}
	}

	override fun parseInput(rawInput: String): List<ReindeerStats> {
		return rawInput.lines().map {
			val (name, speed, flightTime, restTime) = inputPattern.parse4<String, Int, Int, Int>(it)
			return@map ReindeerStats(name, speed, flightTime, restTime)
		}
	}

	private enum class Mode {
		Distance, Score
	}

	@Suppress("NOTHING_TO_INLINE")
	private inline fun task(input: List<ReindeerStats>, seconds: Int, mode: Mode): Int {
		val reindeers = input.map { Reindeer(it) }
		repeat(seconds) {
			reindeers.forEach { it.tick() }
			if (mode == Mode.Score) {
				val sorted = reindeers.sortedByDescending { it.distance }
				sorted.filter { it.distance == sorted.first().distance }.forEach { it.score++ }
			}
		}
		return when (mode) {
			Mode.Distance -> reindeers.map { it.distance }.max()!!
			Mode.Score -> reindeers.map { it.score }.max()!!
		}
	}

	override fun part1(input: List<ReindeerStats>): Int {
		return task(input, 2503, Mode.Distance)
	}

	override fun part2(input: List<ReindeerStats>): Int {
		return task(input, 2503, Mode.Score)
	}

	class Tests {
		private val task = Day14()

		private val rawInput = """
            Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
			Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
		""".trimIndent()

		@Test
		fun part1() {
			Assertions.assertEquals(16, task.task(task.parseInput(rawInput), 1, Mode.Distance))
			Assertions.assertEquals(176, task.task(task.parseInput(rawInput), 11, Mode.Distance))
			Assertions.assertEquals(1120, task.task(task.parseInput(rawInput), 1000, Mode.Distance))
		}

		@Test
		fun part2() {
			Assertions.assertEquals(1, task.task(task.parseInput(rawInput), 1, Mode.Score))
			Assertions.assertEquals(689, task.task(task.parseInput(rawInput), 1000, Mode.Score))
		}
	}
}