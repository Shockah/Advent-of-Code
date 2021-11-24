package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.IntPatternParser
import pl.shockah.aoc.parse
import pl.shockah.aoc.parse6
import java.util.regex.Pattern

class Day4: AdventTask<Map<Int, List<ClosedRange<Day4.Date>>>, Int, Int>(2018, 4) {
	private val baseInputPattern: Pattern = Pattern.compile("\\[(\\d+)-(\\d+)-(\\d+) (\\d+):(\\d+)] (.*)")
	private val shiftPattern: Pattern = Pattern.compile("Guard #(\\d+) begins shift")
	private val fallAsleep: String = "falls asleep"
	private val wakeUp: String = "wakes up"

	data class Date(
			val year: Int,
			val month: Int,
			val day: Int,
			val hour: Int,
			val minute: Int
	): Comparable<Date> {
		override fun compareTo(other: Date): Int {
			if (year != other.year)
				return year.compareTo(other.year)
			if (month != other.month)
				return month.compareTo(other.month)
			if (day != other.day)
				return day.compareTo(other.day)
			if (hour != other.hour)
				return hour.compareTo(other.hour)
			if (minute != other.minute)
				return minute.compareTo(other.minute)
			return 0
		}
	}

	data class LogEntry(
			val date: Date,
			val type: LogType
	): Comparable<LogEntry> {
		override fun compareTo(other: LogEntry): Int {
			return date.compareTo(other.date)
		}
	}

	sealed class LogType {
		data class Shift(
				val guardId: Int
		): LogType()

		object WakeUp: LogType()

		object FallAsleep: LogType()
	}

	private fun preParseInput(rawInput: String, sorting: Boolean): List<LogEntry> {
		var parsed = rawInput.lines().map {
			val (year, month, day, hour, minute, content) = baseInputPattern.parse6<Int, Int, Int, Int, Int, String>(it)

			val type: LogType = when (content) {
				fallAsleep -> LogType.FallAsleep
				wakeUp -> LogType.WakeUp
				else -> {
					val guardId = shiftPattern.parse(content, IntPatternParser)
					LogType.Shift(guardId)
				}
			}

			return@map LogEntry(Date(year, month, day, hour, minute), type)
		}

		if (sorting)
			parsed = parsed.sorted()
		return parsed
	}

	private fun parseInput(input: List<LogEntry>): Map<Int, List<ClosedRange<Date>>> {
		var currentGuard: Int? = null
		var asleepSince: Date? = null
		val sleepPeriods = mutableMapOf<Int, MutableList<ClosedRange<Date>>>()

		outer@ for (entry in input) {
			when (entry.type) {
				is LogType.Shift -> {
					currentGuard = entry.type.guardId
					asleepSince = null
				}
				LogType.FallAsleep -> asleepSince = entry.date
				LogType.WakeUp -> {
					if (currentGuard == null || asleepSince == null)
						continue@outer
					if (sleepPeriods[currentGuard] == null)
						sleepPeriods[currentGuard] = mutableListOf()
					sleepPeriods[currentGuard]!! += asleepSince..entry.date
					asleepSince = null
				}
			}
		}

		return sleepPeriods
	}

	override fun parseInput(rawInput: String): Map<Int, List<ClosedRange<Date>>> {
		return parseInput(preParseInput(rawInput, true))
	}

	private fun countMinuteOccurences(periods: List<ClosedRange<Date>>): IntArray {
		val occurences = IntArray(60)
		for (period in periods) {
			for (minute in period.start.minute until period.endInclusive.minute) {
				occurences[minute]++
			}
		}
		return occurences
	}

	override fun part1(input: Map<Int, List<ClosedRange<Date>>>): Int {
		val guardId = input.map { (guardId, periods) ->
			guardId to periods.sumOf { it.endInclusive.minute - it.start.minute }
		}.maxByOrNull { it.second }!!.first

		val (mostSleptMinute, times) = countMinuteOccurences(input[guardId]!!).mapIndexed { index: Int, i: Int -> index to i }.maxByOrNull { it.second }!!
		println("> Guard ID: $guardId")
		println("> Most slept hour: 00:$mostSleptMinute")
		println("> Times: $times")
		return guardId * mostSleptMinute
	}

	override fun part2(input: Map<Int, List<ClosedRange<Date>>>): Int {
		val (guardId, minuteIndex, times) = input.map { (guardId, periods) ->
			val (minuteIndex, totalTimes) = countMinuteOccurences(periods).mapIndexed { index: Int, i: Int -> index to i }.maxByOrNull { it.second }!!
			return@map Triple(guardId, minuteIndex, totalTimes)
		}.maxByOrNull { it.third }!!
		println("> Guard ID: $guardId")
		println("> Most slept hour: 00:$minuteIndex")
		println("> Times: $times")
		return guardId * minuteIndex
	}

	class Tests {
		private val task = Day4()

		private val rawInput = """
			[1518-11-01 00:00] Guard #10 begins shift
			[1518-11-01 00:05] falls asleep
			[1518-11-01 00:25] wakes up
			[1518-11-01 00:30] falls asleep
			[1518-11-01 00:55] wakes up
			[1518-11-01 23:58] Guard #99 begins shift
			[1518-11-02 00:40] falls asleep
			[1518-11-02 00:50] wakes up
			[1518-11-03 00:05] Guard #10 begins shift
			[1518-11-03 00:24] falls asleep
			[1518-11-03 00:29] wakes up
			[1518-11-04 00:02] Guard #99 begins shift
			[1518-11-04 00:36] falls asleep
			[1518-11-04 00:46] wakes up
			[1518-11-05 00:03] Guard #99 begins shift
			[1518-11-05 00:45] falls asleep
			[1518-11-05 00:55] wakes up
		""".trimIndent()

		@Test
		fun preInputSorting() {
			val input1 = task.preParseInput(rawInput, false)
			val input2 = task.preParseInput(rawInput, true)
			Assertions.assertEquals(input1.toString(), input2.toString())
		}

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(240, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(4455, task.part2(input))
		}
	}
}