package pl.shockah.aoc.y2015

import pl.shockah.aoc.AdventTask
import kotlin.math.max

class Day21: AdventTask<Day21.Stats, Int, Int>(2015, 21) {
	private val playerHealth = 100

	data class Stats(
			val health: Int,
			val damage: Int,
			val armor: Int
	) {
		constructor(
				health: Int,
				equipment: Set<Item>
		): this(health, equipment.sumBy { it.damage }, equipment.sumBy { it.armor })
	}

	private class Character(
			val stats: Stats
	) {
		var health: Int = stats.health

		fun attack(character: Character): Boolean {
			val actualDamage = max(stats.damage - character.stats.armor, 1)
			character.health -= actualDamage
			return character.health <= 0
		}
	}

	enum class ItemType(
			val equipLimit: IntRange
	) {
		Weapon(1..1), Armor(0..1), Ring(0..2)
	}

	data class Item(
			val type: ItemType,
			val name: String,
			val cost: Int,
			val damage: Int = 0,
			val armor: Int = 0
	) {
		override fun toString(): String {
			return "${type.name}: $name"
		}
	}

	private data class Result(
			val playerHealth: Int,
			val enemyHealth: Int,
			val turns: Int
	) {
		override fun toString(): String {
			return if (playerHealth <= 0)
				"Died after $turns turns. Health left: $enemyHealth"
			else
				"Victorious after $turns turns. Health left: $playerHealth"
		}
	}

	private val shop = setOf(
			Item(ItemType.Weapon, "Dagger", 8, damage = 4),
			Item(ItemType.Weapon, "Shortsword", 10, damage = 5),
			Item(ItemType.Weapon, "Warhammer", 25, damage = 6),
			Item(ItemType.Weapon, "Longsword", 40, damage = 7),
			Item(ItemType.Weapon, "Greataxe", 74, damage = 8),

			Item(ItemType.Armor, "Leather", 13, armor = 1),
			Item(ItemType.Armor, "Chainmail", 31, armor = 2),
			Item(ItemType.Armor, "Splintmail", 53, armor = 3),
			Item(ItemType.Armor, "Bandedmail", 75, armor = 4),
			Item(ItemType.Armor, "Platemail", 102, armor = 5),

			Item(ItemType.Ring, "Damage +1", 25, damage = 1),
			Item(ItemType.Ring, "Damage +2", 50, damage = 2),
			Item(ItemType.Ring, "Damage +3", 100, damage = 3),
			Item(ItemType.Ring, "Defense +1", 20, armor = 1),
			Item(ItemType.Ring, "Defense +2", 40, armor = 2),
			Item(ItemType.Ring, "Defense +3", 80, armor = 3)
	)

	override fun parseInput(rawInput: String): Stats {
		val lines = rawInput.lines()
		return Stats(
				lines[0].split(" ").last().toInt(),
				lines[1].split(" ").last().toInt(),
				lines[2].split(" ").last().toInt()
		)
	}

	private fun simulate(stats1: Stats, stats2: Stats): Result {
		val character1 = Character(stats1)
		val character2 = Character(stats2)
		var turns = 0

		while (true) {
			turns++
			if (character1.attack(character2))
				break
			turns++
			if (character2.attack(character1))
				break
		}
		return Result(character1.health, character2.health, turns)
	}

	private fun generateEquipmentSets(): Set<Set<Item>> {
		val typeSets = ItemType.values().map { generateEquipmentTypeSets(it) }.map { it.toList() }
		val variations = typeSets.map { it.size }.reduce { acc, it -> acc * it }
		return (0 until variations).map { i ->
			var index = i
			val fullSet = mutableSetOf<Item>()

			for (typeSet in typeSets) {
				fullSet += typeSet[index % typeSet.size]
				index /= typeSet.size
			}
			return@map fullSet
		}.toSet()
	}

	private fun generateEquipmentTypeSets(type: ItemType): Set<Set<Item>> {
		return type.equipLimit.flatMap { generateEquipmentTypeSets(shop.filter { it.type == type }.toSet(), it) }.toSet()
	}

	private fun generateEquipmentTypeSets(options: Set<Item>, count: Int, existing: Set<Item> = emptySet()): Set<Set<Item>> {
		return when (count) {
			0 -> setOf(existing)
			1 -> options.map { existing + it }.toSet()
			else -> options.flatMap { generateEquipmentTypeSets(options - it, count - 1, existing + it) }.toSet()
		}
	}

	private enum class ExpectedResult {
		CheapWin, ExpensiveLoss
	}

	private fun task(enemyStats: Stats, expectedResult: ExpectedResult): Int {
		var sets = generateEquipmentSets().toList()
		sets = when (expectedResult) {
			ExpectedResult.CheapWin -> sets.sortedBy { it.sumBy { it.cost } }
			ExpectedResult.ExpensiveLoss -> sets.sortedByDescending { it.sumBy { it.cost } }
		}

		for (set in sets) {
			println("${set.joinToString()} (cost: ${set.sumBy { it.cost }}, damage: ${set.sumBy { it.damage }}, armor: ${set.sumBy { it.armor }})")
			val playerStats = Stats(playerHealth, set)
			val result = simulate(playerStats, enemyStats)

			println("\t$result")
			when (expectedResult) {
				ExpectedResult.CheapWin -> {
					if (result.playerHealth > 0)
						return set.sumBy { it.cost }
				}
				ExpectedResult.ExpensiveLoss -> {
					if (result.enemyHealth > 0)
						return set.sumBy { it.cost }
				}
			}
		}

		throw IllegalStateException()
	}

	override fun part1(input: Stats): Int {
		return task(input, ExpectedResult.CheapWin)
	}

	override fun part2(input: Stats): Int {
		return task(input, ExpectedResult.ExpensiveLoss)
	}
}