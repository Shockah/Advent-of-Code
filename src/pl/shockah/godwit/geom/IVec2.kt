package pl.shockah.godwit.geom

import pl.shockah.godwit.ease.Easable
import pl.shockah.godwit.ease.Easing
import kotlin.math.atan2
import kotlin.math.sqrt

typealias Vec2 = IVec2<*>

abstract class IVec2<T : IVec2<T>> : Comparable<IVec2<*>>, Easable<IVec2<*>> {
	abstract val x: Float
	abstract val y: Float

	val lengthSquared: Float
		get() = x * x + y * y

	open val length: Float
		get() = sqrt(lengthSquared)

	open val angle: Degrees
		get() = ImmutableVec2.ZERO angle this

	@Suppress("UNCHECKED_CAST")
	val normalized: T
		get() {
			val length = length
			return when (length) {
				0f -> throw IllegalStateException()
				1f -> this as T
				else -> this / length
			}
		}

	operator fun get(index: Int): Float = when (index) {
		0 -> x
		1 -> y
		else -> throw IllegalArgumentException()
	}

	operator fun component1(): Float = x
	operator fun component2(): Float = y

	fun immutableCopy(): ImmutableVec2 = ImmutableVec2(x, y)
	fun mutableCopy(): MutableVec2 = MutableVec2(x, y)

	abstract operator fun unaryMinus(): T

	abstract operator fun plus(vector: Vec2): T
	abstract operator fun minus(vector: Vec2): T
	abstract operator fun times(vector: Vec2): T
	abstract operator fun div(vector: Vec2): T

	abstract operator fun plus(scalar: Float): T
	abstract operator fun minus(scalar: Float): T
	abstract operator fun times(scalar: Float): T
	abstract operator fun div(scalar: Float): T

	infix fun angle(vector: Vec2): Degrees {
		return Degrees(Math.toDegrees(atan2((y - vector.y).toDouble(), (vector.x - x).toDouble())).toFloat())
	}

	infix fun dot(vector: Vec2): Float {
		return x * vector.x + y * vector.y
	}

	infix fun cross(vector: Vec2): Float {
		return x * vector.y - y * vector.x
	}

	infix fun equals(other: Vec2): Boolean {
		return other.x == x && other.y == y
	}

	override fun equals(other: Any?): Boolean {
		return other is Vec2 && other.x == x && other.y == y
	}

	override fun hashCode(): Int {
		return x.hashCode() * 31 + y.hashCode()
	}

	override fun toString(): String = "[IVec2(x: $x, y: $y)]"

	override fun compareTo(other: IVec2<*>): Int {
		return length.compareTo(other.length)
	}

	override fun ease(other: IVec2<*>, f: Float): IVec2<*> {
		return ImmutableVec2(
				Easing.linear.ease(x, other.x, f),
				Easing.linear.ease(y, other.y, f)
		)
	}
}