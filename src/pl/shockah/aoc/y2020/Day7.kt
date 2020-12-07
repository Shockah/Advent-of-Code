package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse2OrNull
import pl.shockah.aoc.parseOrNull
import java.util.*
import java.util.regex.Pattern

class Day7: AdventTask<Map<String, Day7.Bag>, Int, Int>(2020, 7) {
	private val nonBagContentPattern = Pattern.compile("(\\w+ \\w+) bags contain no other bags.")
	private val bagContentPattern = Pattern.compile("(\\w+ \\w+) bags contain ((?:(, )?\\d+ \\w+ \\w+ bags?)+)\\.")
	private val bagContentDetailPattern = Pattern.compile("(\\d+) (\\w+ \\w+) bags?")

	private data class Rule(
		val containerBag: String,
		val containedBags: Map<String, Int>
	)

	class Bag(
		val key: String,
	) {
		val containerBags = mutableSetOf<Bag>()
		val containedBags = mutableMapOf<Bag, Int>()

		override fun toString(): String {
			return key
		}
	}

	override fun parseInput(rawInput: String): Map<String, Bag> {
		val rules = rawInput.lines().map { line ->
			run {
				val bagContent = bagContentPattern.parse2OrNull<String, String>(line)
				if (bagContent != null) {
					val (containerBag, containedBags) = bagContent
					val containedBagsMap = mutableMapOf<String, Int>()
					val matcher = bagContentDetailPattern.matcher(containedBags)
					while (matcher.find()) {
						val containedBagCount = matcher.group(1).toInt()
						val containedBag = matcher.group(2)
						containedBagsMap[containedBag] = containedBagCount
					}
					return@map Rule(containerBag, containedBagsMap)
				}
			}

			run {
				val nonBagContent = nonBagContentPattern.parseOrNull<String>(line)
				if (nonBagContent != null) {
					return@map Rule(nonBagContent, emptyMap())
				}
			}

			throw IllegalArgumentException()
		}

		val bags = mutableMapOf<String, Bag>()

		fun getBag(key: String): Bag {
			return bags.getOrPut(key) { Bag(key) }
		}

		for (rule in rules) {
			val containerBag = getBag(rule.containerBag)
			for ((containedBagKey, containedBagCount) in rule.containedBags) {
				val containedBag = getBag(containedBagKey)
				containerBag.containedBags[containedBag] = containedBagCount
				containedBag.containerBags.add(containerBag)
			}
		}
		return bags
	}

	private fun countContainers(containedBag: Bag): Int {
		val toCheck = LinkedList<Bag>()
		val checked = mutableSetOf<Bag>()
		toCheck.addAll(containedBag.containerBags)

		while (!toCheck.isEmpty()) {
			val containerBag = toCheck.removeFirst()
			if (checked.contains(containedBag))
				continue
			checked.add(containerBag)
			toCheck.addAll(containerBag.containerBags)
		}
		return checked.size
	}

	override fun part1(input: Map<String, Bag>): Int {
		val myBag = input["shiny gold"]!!
		val toCheck = LinkedList<Bag>()
		val checked = mutableSetOf<Bag>()
		toCheck.addAll(myBag.containerBags)

		while (toCheck.isNotEmpty()) {
			val containerBag = toCheck.removeFirst()
			if (checked.contains(containerBag))
				continue
			checked.add(containerBag)
			toCheck.addAll(containerBag.containerBags)
		}
		return checked.size
	}

	override fun part2(input: Map<String, Bag>): Int {
		val myBag = input["shiny gold"]!!
		val totalContainedBagCounts = mutableMapOf<Bag, Int>()
		val toCheck = myBag.containedBags.toMutableMap()
		while (toCheck.isNotEmpty()) {
			val (containedBag, containedBagCount) = toCheck.entries.first()
			toCheck.remove(containedBag)
			totalContainedBagCounts[containedBag] = (totalContainedBagCounts[containedBag] ?: 0) + containedBagCount
			for ((nestedContainedBag, nestedContainedBagCount) in containedBag.containedBags) {
				val count = (toCheck[nestedContainedBag] ?: 0) + nestedContainedBagCount * containedBagCount
				if (count > 0)
					toCheck[nestedContainedBag] = count
				else
					toCheck.remove(nestedContainedBag)
			}
		}
		return totalContainedBagCounts.values.sum()
	}

	class Tests {
		private val task = Day7()

		private val rawInput1 = """
			light red bags contain 1 bright white bag, 2 muted yellow bags.
			dark orange bags contain 3 bright white bags, 4 muted yellow bags.
			bright white bags contain 1 shiny gold bag.
			muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
			shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
			dark olive bags contain 3 faded blue bags, 4 dotted black bags.
			vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
			faded blue bags contain no other bags.
			dotted black bags contain no other bags.
		""".trimIndent()

		private val rawInput2 = """
			shiny gold bags contain 2 dark red bags.
			dark red bags contain 2 dark orange bags.
			dark orange bags contain 2 dark yellow bags.
			dark yellow bags contain 2 dark green bags.
			dark green bags contain 2 dark blue bags.
			dark blue bags contain 2 dark violet bags.
			dark violet bags contain no other bags.
		""".trimIndent()

		@Test
		fun part1ForInput1() {
			val input = task.parseInput(rawInput1)
			Assertions.assertEquals(4, task.part1(input))
		}

		@Test
		fun part1ForInput2() {
			val input = task.parseInput(rawInput2)
			Assertions.assertEquals(0, task.part1(input))
		}

		@Test
		fun part2ForInput1() {
			val input = task.parseInput(rawInput1)
			Assertions.assertEquals(32, task.part2(input))
		}

		@Test
		fun part2ForInput2() {
			val input = task.parseInput(rawInput2)
			Assertions.assertEquals(126, task.part2(input))
		}
	}
}