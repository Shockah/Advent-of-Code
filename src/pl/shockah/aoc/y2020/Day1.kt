package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.expects
import pl.shockah.unikorn.collection.sumByLong

class Day1: AdventTask<List<Int>, Int, Int>(2020, 1) {
    override fun parseInput(rawInput: String): List<Int> {
        return rawInput.lines().filter { it.isNotEmpty() }.map { it.toInt() }
    }

    private fun task(input: List<Int>, firstIndex: Int = 0, current: List<Int> = emptyList(), toTake: Int, expectedSum: Int): Int? {
        if (toTake <= 0) {
            return if (current.sum() == expectedSum) current.reduce(Int::times) else null
        } else {
            for (i in firstIndex until input.size) {
                val newCurrent = current + input[i]
                val result = task(input, i + 1, newCurrent, toTake - 1, expectedSum)
                if (result != null)
                    return result
            }
            return null
        }
    }

    override fun part1(input: List<Int>): Int {
        return task(input, toTake = 2, expectedSum = 2020) ?: throw IllegalArgumentException("No pair of numbers results in a sum of 2020")
    }

    override fun part2(input: List<Int>): Int {
        return task(input, toTake = 3, expectedSum = 2020) ?: throw IllegalArgumentException("No pair of numbers results in a sum of 2020")
    }

    class Tests {
        private val task = Day1()

        @TestFactory
        fun part1(): Collection<DynamicTest> = createTestCases(
                "1721\n979\n366\n299\n675\n1456" expects 514579
        ) { rawInput, expected ->
            val input = task.parseInput(rawInput)
            Assertions.assertEquals(expected, task.part1(input))
        }
    }
}