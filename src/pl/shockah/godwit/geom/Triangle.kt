package pl.shockah.godwit.geom

import pl.shockah.godwit.ease.Easable
import pl.shockah.godwit.geom.polygon.ClosedPolygon
import pl.shockah.godwit.geom.polygon.Polygonable

class Triangle(
	point1: Vec2,
	point2: Vec2,
	point3: Vec2
) : Polygonable.Closed, Easable<Triangle> {
	val points: List<MutableVec2> = listOf(point1.mutableCopy(), point2.mutableCopy(), point3.mutableCopy())

	override val boundingBox: Rectangle
		get() {
			val minX = minOf(points[0].x, points[1].x, points[2].x)
			val minY = minOf(points[0].y, points[1].y, points[2].y)
			val maxX = maxOf(points[0].x, points[1].x, points[2].x)
			val maxY = maxOf(points[0].y, points[1].y, points[2].y)
			return Rectangle(ImmutableVec2(minX, minY), ImmutableVec2(maxX - minX, maxY - minY))
		}

	val lines: List<Line>
		get() = listOf(
				Line(points[0], points[1]),
				Line(points[1], points[2]),
				Line(points[2], points[3])
		)

	override fun copy(): Triangle = Triangle(points[0], points[1], points[2])

	override fun equals(other: Any?): Boolean {
		return other is Triangle && points == other.points
	}

	override fun hashCode(): Int {
		return points.hashCode()
	}

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

	override operator fun contains(point: Vec2): Boolean {
		fun sign(testedPoint: Vec2, point1: Vec2, point2: Vec2): Float {
			return (testedPoint.x - point2.x) * (point1.y - point2.y) - (point1.x - point2.x) * (testedPoint.y - point2.y)
		}

		val b1 = sign(point, points[0], points[1]) < 0f
		val b2 = sign(point, points[1], points[2]) < 0f
		val b3 = sign(point, points[2], points[0]) < 0f
		return b1 == b2 && b2 == b3
	}

	private companion object Collisions {
		init {
			Shape.registerCollisionHandler(Triangle::class, Triangle::class) { a, b ->
				for (point in b.points) {
					if (point in a)
						return@registerCollisionHandler true
				}
				for (aLine in a.lines) {
					for (bLine in b.lines) {
						if (bLine collides aLine)
							return@registerCollisionHandler true
					}
				}
				return@registerCollisionHandler false
			}
			Shape.registerCollisionHandler(Triangle::class, Line::class) { triangle, line ->
				if (line.point1 in triangle || line.point2 in triangle)
					return@registerCollisionHandler true
				for (triangleLine in triangle.lines) {
					if (line collides triangleLine)
						return@registerCollisionHandler true
				}
				return@registerCollisionHandler false
			}
		}
	}

	override fun asClosedPolygon(): ClosedPolygon {
		return ClosedPolygon(points)
	}

	override fun ease(other: Triangle, f: Float): Triangle {
		return Triangle(
				points[0].ease(other.points[0], f),
				points[1].ease(other.points[1], f),
				points[2].ease(other.points[2], f)
		)
	}
}