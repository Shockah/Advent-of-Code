package pl.shockah.aoc.y2019

import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.toCharString
import pl.shockah.unikorn.collection.Array2D

class Day8: AdventTask<List<Array2D<Int>>, Int, String>(2019, 8) {
	override fun parseInput(rawInput: String): List<Array2D<Int>> {
		return parseInput(rawInput, 25, 6)
	}

	private fun parseInput(rawInput: String, width: Int, height: Int): List<Array2D<Int>> {
		val chars = rawInput.map { it - '0' }
		return chars.chunked(width * height).map { Array2D(width, height) { x, y -> it[x + y * width] } }
	}

	override fun part1(input: List<Array2D<Int>>): Int {
		val layer = input.minByOrNull { it.toList().count { it == 0 } }!!
		val list = layer.toList()
		return list.count { it == 1 } * list.count { it == 2 }
	}

	override fun part2(input: List<Array2D<Int>>): String {
		val width = input[0].width
		val height = input[0].height
		return Array2D(width, height) { x, y ->
			for (layer in input) {
				val color = Color.byId[layer[x, y]]!!
				if (color != Color.Transparent)
					return@Array2D Color.byId[layer[x, y]]!!.symbol
			}
			return@Array2D Color.Transparent.symbol
		}.toCharString()
	}

	private enum class Color(
		val id: Int,
		val symbol: Char
	) {
		Black(0, '.'), White(1, '#'), Transparent(2, '?');

		companion object {
			val byId = values().associateBy { it.id }
		}
	}
}