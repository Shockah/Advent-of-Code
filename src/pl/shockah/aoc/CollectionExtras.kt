package pl.shockah.aoc

fun <T : Comparable<T>> Array<T>.maxIndex(): Int? {
	if (size == 0)
		return null

	var max = this[0]
	var index = 0
	for (i in 1 until size) {
		if (this[i] > max) {
			max = this[i]
			index = i
		}
	}
	return index
}

fun IntArray.maxIndex(): Int? {
	if (size == 0)
		return null

	var max = this[0]
	var index = 0
	for (i in 1 until size) {
		if (this[i] > max) {
			max = this[i]
			index = i
		}
	}
	return index
}

fun <T : Comparable<T>> Array<T>.minIndex(): Int? {
	if (size == 0)
		return null

	var min = this[0]
	var index = 0
	for (i in 1 until size) {
		if (this[i] < min) {
			min = this[i]
			index = i
		}
	}
	return index
}

fun IntArray.minIndex(): Int? {
	if (size == 0)
		return null

	var min = this[0]
	var index = 0
	for (i in 1 until size) {
		if (this[i] < min) {
			min = this[i]
			index = i
		}
	}
	return index
}