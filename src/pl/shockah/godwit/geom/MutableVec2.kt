package pl.shockah.godwit.geom

import kotlin.math.cos
import kotlin.math.sin

class MutableVec2(
		override var x: Float,
		override var y: Float
) : IVec2<MutableVec2>() {
	constructor() : this(0f, 0f)

	companion object {
		fun angled(degrees: Degrees, length: Float): MutableVec2 = MutableVec2(
				(-cos(Math.toRadians((degrees.value + 180f).toDouble())) * length).toFloat(),
				(-sin(Math.toRadians((degrees.value + 180f).toDouble())) * length).toFloat()
		)
	}

	override var length: Float
		get() = super.length
		set(value) {
			val angle = angle
			x = (-cos(Math.toRadians((angle.value + 180f).toDouble())) * value).toFloat()
			y = (-sin(Math.toRadians((angle.value + 180f).toDouble())) * value).toFloat()
		}

	override var angle: Degrees
		get() = super.angle
		set(value) {
			val length = length
			x = (-cos(Math.toRadians((value.value + 180f).toDouble())) * length).toFloat()
			y = (-sin(Math.toRadians((value.value + 180f).toDouble())) * length).toFloat()
		}

	val xy: Mutator
		get() = Mutator()

	operator fun set(index: Int, value: Float) = when (index) {
		0 -> x = value
		1 -> y = value
		else -> throw IllegalArgumentException()
	}

	override operator fun unaryMinus(): MutableVec2 = MutableVec2(-x, -y)

	override operator fun plus(vector: Vec2): MutableVec2 = MutableVec2(x + vector.x, y + vector.y)
	override operator fun minus(vector: Vec2): MutableVec2 = MutableVec2(x - vector.x, y - vector.y)
	override operator fun times(vector: Vec2): MutableVec2 = MutableVec2(x * vector.x, y * vector.y)
	override operator fun div(vector: Vec2): MutableVec2 = MutableVec2(x / vector.x, y / vector.y)

	override operator fun plus(scalar: Float): MutableVec2 = normalized * (length + scalar)
	override operator fun minus(scalar: Float): MutableVec2 = normalized * (length - scalar)
	override operator fun times(scalar: Float): MutableVec2 = MutableVec2(x * scalar, y * scalar)
	override operator fun div(scalar: Float): MutableVec2 = MutableVec2(x / scalar, y / scalar)

	inner class Mutator {
		operator fun plusAssign(vector: Vec2) {
			x += vector.x
			y += vector.y
		}

		operator fun minusAssign(vector: Vec2) {
			x -= vector.x
			y -= vector.y
		}

		operator fun timesAssign(vector: Vec2) {
			x *= vector.x
			y *= vector.y
		}

		operator fun divAssign(vector: Vec2) {
			x /= vector.x
			y /= vector.y
		}

		operator fun plusAssign(scalar: Float) {
			val result = normalized * (length + scalar)
			x = result.x
			y = result.y
		}

		operator fun minusAssign(scalar: Float) {
			val result = normalized * (length - scalar)
			x = result.x
			y = result.y
		}

		operator fun timesAssign(scalar: Float) {
			x *= scalar
			y *= scalar
		}

		operator fun divAssign(scalar: Float) {
			x /= scalar
			y /= scalar
		}
	}
}