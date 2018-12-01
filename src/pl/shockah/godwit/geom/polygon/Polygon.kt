package pl.shockah.godwit.geom.polygon

import pl.shockah.godwit.ObservableList
import pl.shockah.godwit.ease.Easable
import pl.shockah.godwit.geom.*
import pl.shockah.godwit.swift.let

open class Polygon(
		points: List<Vec2>
) : Shape.Outline, Easable<Polygon> {
	val points: ObservableList<MutableVec2> = ObservableList(points.map { it.mutableCopy() }.toMutableList())

	constructor(vararg points: Vec2) : this(points.toMutableList())

	override val boundingBox: Rectangle
		get() {
			let(
					points.map { it.x }::min,
					points.map { it.y }::min,
					points.map { it.x }::max,
					points.map { it.y }::max
			) { minX, minY, maxX, maxY ->
				return Rectangle(ImmutableVec2(minX, minY), ImmutableVec2(maxX - minX, maxY - minY))
			}
			return Rectangle(ImmutableVec2.ZERO, ImmutableVec2.ZERO)
		}

	open val lines: List<Line>
		get() {
			val result: MutableList<Line> = mutableListOf()
			for (i in 0 until (points.size - 1)) {
				result += Line(points[i], points[i + 1])
			}
			return result
		}

	override fun copy(): Polygon = Polygon(points)

	override fun translate(vector: Vec2) {
		points.forEach { it.xy += vector }
	}

	override fun mirror(horizontal: Boolean, vertical: Boolean) {
		if (horizontal)
			points.forEach { it.x = -it.x }
		if (vertical)
			points.forEach { it.y = -it.y }
	}

	override fun scale(scale: Float) {
		points.forEach { it.xy *= scale }
	}

	private companion object Collisions {
		init {
			Shape.registerCollisionHandler(Polygon::class, Polygon::class) { a, b ->
				for (aLine in a.lines) {
					for (bLine in b.lines) {
						if (aLine collides bLine)
							return@registerCollisionHandler true
					}
				}
				return@registerCollisionHandler false
			}
			Shape.registerCollisionHandler(Polygon::class, Line::class) { polygon, line ->
				for (polygonLine in polygon.lines) {
					if (polygonLine collides line)
						return@registerCollisionHandler true
				}
				return@registerCollisionHandler false
			}
		}
	}

	override fun ease(other: Polygon, f: Float): Polygon {
		if (points.size != other.points.size)
			throw IllegalArgumentException()
		return Polygon(points.mapIndexed { index, point -> point.ease(other.points[index], f) })
	}
}