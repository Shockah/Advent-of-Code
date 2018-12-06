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

	@Suppress("NOTHING_TO_INLINE")
	private inline fun getTotalCost(ingredients: Map<Ingredient, Int>, zeroOut: Boolean): Int {
		val capacity = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.capacity * amount }
		val durability = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.durability * amount }
		val flavor = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.flavor * amount }
		val texture = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.texture * amount }

		return if (zeroOut)
			max(capacity, 0) * max(durability, 0) * max(flavor, 0) * max(texture, 0)
		else
			capacity * durability * flavor * texture
	}

	private fun findBestIngredients(input: List<Ingredient>, ingredients: Map<Ingredient, Int>, spoonsLeft: Int): Map<Ingredient, Int> {
		if (spoonsLeft == 0)
			return ingredients

		operator fun Map<Ingredient, Int>.plus(ingredient: Ingredient): Map<Ingredient, Int> {
			val result = toMutableMap()
			result[ingredient] = (result[ingredient] ?: 0) + 1
			return result
		}

		val capacity = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.capacity * amount }
		val durability = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.durability * amount }
		val flavor = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.flavor * amount }
		val texture = ingredients.entries.sumBy { (ingredient, amount) -> ingredient.texture * amount }
		val min = arrayOf(capacity, durability, flavor, texture, 0).min()!!

		fun getBest(filteredInput: List<Ingredient>): Map<Ingredient, Int> {
			return filteredInput.map {
				findBestIngredients(input, ingredients + it, spoonsLeft - 1)
			}.maxBy {
				getTotalCost(it, false)
			}!!
		}

		return when (min) {
			capacity -> getBest(input.filter { it.capacity > 0 })
			durability -> getBest(input.filter { it.durability > 0 })
			flavor -> getBest(input.filter { it.flavor > 0 })
			texture -> getBest(input.filter { it.texture > 0 })
			else -> getBest(input)
		}
	}

	override fun part1(input: List<Ingredient>): Int {
		return getTotalCost(findBestIngredients(input, mapOf(), 100), true)
	}

	override fun part2(input: List<Ingredient>): Int {
		TODO()
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
	}
}