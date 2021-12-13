package pl.shockah.aoc

import pl.shockah.unikorn.collection.Array2D

fun <T> Array2D<T>.toCharString(mappingFunction: (element: T) -> Char): String {
	return (0 until height).joinToString("\n") { y ->
		(0 until width).joinToString("") { x ->
			"${mappingFunction(this[x, y])}"
		}
	}
}

fun <T> Array2D<T>.toCharString(mappingFunction: (array: Array2D<T>, x: Int, y: Int, element: T) -> Char): String {
	return (0 until height).joinToString("\n") { y ->
		(0 until width).joinToString("") { x ->
			"${mappingFunction(this, x, y, this[x, y])}"
		}
	}
}

fun <T> Array2D<T>.toCharString(emptyValue: T, emptyCharacter: Char, nonEmptyCharacter: Char): String {
	return toCharString { value -> if (value == emptyValue) emptyCharacter else nonEmptyCharacter }
}

fun Array2D<Char>.toCharString(): String {
	return toCharString { value -> value }
}