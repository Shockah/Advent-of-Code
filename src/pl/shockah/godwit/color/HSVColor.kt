package pl.shockah.godwit.color

import pl.shockah.godwit.geom.Degrees
import kotlin.math.pow
import kotlin.math.sqrt

data class HSVColor(
		var h: Float,
		var s: Float,
		var v: Float
) : ColorSpace<HSVColor> {
	companion object {
		fun from(rgb: RGBColor): HSVColor {
			val max = maxOf(rgb.r, rgb.g, rgb.b)
			val min = minOf(rgb.r, rgb.g, rgb.b)
			val range = max - min

			val h: Float
			val s: Float
			val v: Float
			h = when {
				range == 0f -> 0f
				max == rgb.r -> (60 * (rgb.g - rgb.b) / range + 360) % 360
				max == rgb.g -> 60 * (rgb.b - rgb.r) / range + 120
				else -> 60 * (rgb.r - rgb.g) / range + 240
			}

			s = if (max > 0) 1 - min / max else 0f
			v = max

			return HSVColor(h / 360f, s, v)
		}

		val RGBColor.hsv: HSVColor
			get() = from(this)
	}

	override fun copy(): HSVColor = HSVColor(h, s, v)

	override val rgb: RGBColor
		get() {
			val h = this.h * 360f
			val x = (h / 60f + 6) % 6
			val i = x.toInt()
			val f = x - i
			val p = v * (1 - s)
			val q = v * (1 - s * f)
			val t = v * (1 - s * (1 - f))
			return when (i) {
				0 -> RGBColor(v, t, p)
				1 -> RGBColor(q, v, p)
				2 -> RGBColor(p, v, t)
				3 -> RGBColor(p, q, v)
				4 -> RGBColor(t, p, v)
				else -> RGBColor(v, p, q)
			}
		}

	override fun getDistance(other: HSVColor): Float {
		var delta = Degrees(h * 360f) delta Degrees(other.h * 360f)
		if (delta.value < 0)
			delta = Degrees(delta.value + 360f)
		return sqrt((delta.value / 360f).pow(2) + (s - other.s).pow(2) + (v - other.v).pow(2))
	}

	override fun ease(other: HSVColor, f: Float): HSVColor {
		val delta = Degrees(h * 360f) delta Degrees(other.h * 360f)
		val h2 = if (delta.value >= 0) other.h else other.h - 1f
		var h = this.h.ease(h2, f)
		if (h < 0)
			h += 1f
		if (h > 0)
			h -= 1f

		return HSVColor(
				h,
				s.ease(other.s, f),
				v.ease(other.v, f)
		)
	}
}