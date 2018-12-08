package pl.shockah.aoc.y2015

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse6
import java.util.regex.Pattern
import kotlin.math.max

class Day15 : AdventTask<List<Day15.Ingredient>, Int, Int>(2015, 15) {
	private val inputPattern: Pattern = Pattern.compile("(.*): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)")

	data class Ingredient(
			val name: String,
			val capacity: Int,
			val durability: Int,
			val flavor: Int,
			val texture: Int,
			val calories: Int
	)

	override fun parseInput(rawInput: String): List<Ingredient> {
		return rawInput.lines().map {
			val (name, capacity, durability, flavor, texture, calories) = inputPattern.parse6<String, Int, Int, Int, Int, Int>(it)
			return@map Ingredient(name, capacity, durability, flavor, texture, calories)
		}.sortedByDescending { it.capacity + it.durability + it.flavor + it.texture }
	}

	private fun getTotalCost(ingredients: Map<Ingredient, Int>): Int {
		val capacity = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.capacity * amount }
		val durability = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.durability * amount }
		val flavor = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.flavor * amount }
		val texture = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.texture * amount }

		return max(capacity, 0) * max(durability, 0) * max(flavor, 0) * max(texture, 0)
	}

	private fun getCalories(ingredients: Map<Ingredient, Int>): Int {
		return ingredients.entries.sumBy { (ingredient, amount) -> ingredient.calories * amount }
	}

	private fun findBestIngredients(input: List<Ingredient>, current: Map<Ingredient, Int>, spoonsLeft: Int, calories: Int?): Map<Ingredient, Int>? {
		if (input.isEmpty())
			return current

		val ingredient = input.first()
		val newInput = input.subList(1, input.size)

		var results = (0..spoonsLeft).mapNotNull { findBestIngredients(newInput, current + (ingredient to it), spoonsLeft - it, calories) }
		if (calories != null)
			results = results.filter { getCalories(it) == calories }
		return results.maxBy { getTotalCost(it) }
	}

	override fun part1(input: List<Ingredient>): Int {
		return getTotalCost(findBestIngredients(input, mapOf(), 100, null)!!)
	}

	override fun part2(input: List<Ingredient>): Int {
		return getTotalCost(findBestIngredients(input, mapOf(), 100, 500)!!)
	}

	class Tests {
		private val task = Day15()

		private val rawInput = """
            Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
			Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(62842880, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(57600000, task.part2(input))
		}
	}
}