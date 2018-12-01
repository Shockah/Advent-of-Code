package pl.shockah.godwit.geom.polygon

import pl.shockah.godwit.geom.Triangle
import pl.shockah.godwit.geom.Vec2

interface Triangulator {
	fun triangulate(points: List<Vec2>): List<Triangle>?
}