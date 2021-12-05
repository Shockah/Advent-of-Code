package pl.shockah.aoc

data class UnorderedPair<T>(
	val first: T,
	val second: T
) {
	override fun equals(other: Any?): Boolean {
		return other is UnorderedPair<*> && ((first == other.first && second == other.second) || (first == other.second && second == other.first))
	}

	override fun hashCode(): Int {
		return first.hashCode() xor second.hashCode()
	}
}