package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.Ref

class Day24: AdventTask<List<Int>, Long, Long>(2015, 24) {
	private data class Group(
			val list: List<Int>
	): List<Int> by list, Comparable<Group> {
		val sum by lazy { sum() }

		val product by lazy { list.map { it.toLong() }.reduce { acc, i -> acc * i} }

		constructor(): this(emptyList())

		operator fun plus(`package`: Int): Group {
			return Group(list + `package`)
		}

		override fun compareTo(other: Group): Int {
			if (size != other.size)
				return size.compareTo(other.size)
			if (product != other.product)
				return product.compareTo(other.product)
			return 0
		}
	}

	private data class Grouping(
			val groups: List<Group>
	) {
		val isValid: Boolean
			get() = groups.map { it.sum }.toSet().size == 1

		constructor(groupCount: Int): this(generateSequence { Group() }.take(groupCount).toList())
	}

	override fun parseInput(rawInput: String): List<Int> {
		return rawInput.lines().map(String::toInt)
	}

	private fun getBestCombination(groupCount: Int, packages: List<Int>): Grouping {
		val best = Ref<Grouping?>(null)
		findBestCombination(packages.sortedDescending(), packages.sum() / groupCount, Grouping(groupCount), best)
		return best.value!!
	}

	private fun findBestCombination(packages: List<Int>, target: Int, grouping: Grouping, best: Ref<Grouping?>) {
		if (best.value != null) {
			if (best.value!!.groups.first() < grouping.groups.first())
				return
		}
		if (packages.isEmpty()) {
			@Suppress("LiftReturnOrAssignment")
			if (grouping.isValid) {
				if (best.value == null || grouping.groups.first() < best.value!!.groups.first()) {
					println("New best: $grouping")
					best.value = grouping
				}
			}
			return
		}

		val descending = grouping.groups.sortedByDescending { it.sum }
		val delta = (descending[0].sum - descending[1].sum) + (descending[1].sum - descending[2].sum)
		if (delta > packages.sum())
			return

		val `package` = packages.first()
		val newPackages = packages.drop(1)

		for (i in grouping.groups.indices) {
			if (grouping.groups[i].sum + `package` <= target) {
				val newGroups = grouping.groups.toMutableList()
				newGroups[i] = newGroups[i] + `package`
				findBestCombination(newPackages, target, Grouping(newGroups), best)
			}
		}
	}

	override fun part1(input: List<Int>): Long {
		return getBestCombination(3, input).groups.first().product
	}

	// TODO: this takes way too long... find a faster way or some optimizations
	override fun part2(input: List<Int>): Long {
		return getBestCombination(4, input).groups.first().product
	}

	class Tests {
		private val task = Day24()

		private val input = (1..5) + (7..11)

		@Test
		fun part1() {
			Assertions.assertEquals(99L, task.part1(input))
		}

		@Test
		fun part2() {
			Assertions.assertEquals(44L, task.part2(input))
		}
	}
}