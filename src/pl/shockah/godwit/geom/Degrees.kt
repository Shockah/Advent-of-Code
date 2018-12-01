package pl.shockah.godwit.geom

import pl.shockah.godwit.ease.Easable

inline class Degrees(
		val value: Float
) : Easable<Degrees> {
	infix fun delta(angle: Degrees): Degrees {
		var angle1 = value
		var angle2 = angle.value
		while (angle2 <= -180)
			angle2 += 360f
		while (angle2 > 180)
			angle2 -= 360f
		while (angle1 <= -180)
			angle1 += 360f
		while (angle1 > 180)
			angle1 -= 360f

		val r = angle2 - angle1
		return Degrees(r + if (r > 180) -360 else if (r < -180) 360 else 0)
	}

	override fun ease(other: Degrees, f: Float): Degrees {
		val delta = this delta other
		return if (delta.value > 0)
			Degrees(value.ease(other.value, f))
		else
			Degrees((value + 360).ease(other.value, f))
	}
}