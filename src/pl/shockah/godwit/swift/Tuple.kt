package pl.shockah.godwit.swift

typealias Tuple2<A, B> = Pair<A, B>
typealias Tuple3<A, B, C> = Triple<A, B, C>

data class Tuple4<out A, out B, out C, out D>(
		val first: A,
		val second: B,
		val third: C,
		val fourth: D
) {
	override fun toString(): String = "($first, $second, $third, $fourth)"
}