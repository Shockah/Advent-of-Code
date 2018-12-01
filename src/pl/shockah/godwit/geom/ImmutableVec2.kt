package pl.shockah.godwit.geom

import kotlin.math.cos
import kotlin.math.sin

class ImmutableVec2(
		override val x: Float,
		override val y: Float
) : IVec2<ImmutableVec2>() {
	constructor() : this(0f, 0f)

	companion object {
		val ZERO: ImmutableVec2 = ImmutableVec2()

		fun angled(degrees: Degrees, length: Float): ImmutableVec2 = ImmutableVec2(
				(-cos(Math.toRadians((degrees.value + 180f).toDouble())) * length).toFloat(),
				(-sin(Math.toRadians((degrees.value + 180f).toDouble())) * length).toFloat()
		)
	}

	override operator fun unaryMinus(): ImmutableVec2 = ImmutableVec2(-x, -y)

	override operator fun plus(vector: Vec2): ImmutableVec2 = ImmutableVec2(x + vector.x, y + vector.y)
	override operator fun minus(vector: Vec2): ImmutableVec2 = ImmutableVec2(x - vector.x, y - vector.y)
	override operator fun times(vector: Vec2): ImmutableVec2 = ImmutableVec2(x * vector.x, y * vector.y)
	override operator fun div(vector: Vec2): ImmutableVec2 = ImmutableVec2(x / vector.x, y / vector.y)

	override operator fun plus(scalar: Float): ImmutableVec2 = normalized * (length + scalar)
	override operator fun minus(scalar: Float): ImmutableVec2 = normalized * (length - scalar)
	override operator fun times(scalar: Float): ImmutableVec2 = ImmutableVec2(x * scalar, y * scalar)
	override operator fun div(scalar: Float): ImmutableVec2 = ImmutableVec2(x / scalar, y / scalar)
}