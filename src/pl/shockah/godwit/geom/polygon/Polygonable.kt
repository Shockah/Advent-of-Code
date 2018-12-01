package pl.shockah.godwit.geom.polygon

import pl.shockah.godwit.geom.Shape

class Polygonable private constructor() {
	interface Open : Shape.Outline {
		fun asPolygon(): Polygon
	}

	interface Closed : Shape.Filled, Open {
		fun asClosedPolygon(): ClosedPolygon

		override fun asPolygon(): Polygon {
			val closedPolygon = asClosedPolygon()
			return Polygon(listOf(*closedPolygon.points.toTypedArray(), closedPolygon.points.first()))
		}
	}
}