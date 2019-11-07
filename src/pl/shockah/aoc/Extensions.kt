package pl.shockah.aoc

infix fun <T : Comparable<T>> T.compareWith(other: T): Int {
	return compareTo(other)
}