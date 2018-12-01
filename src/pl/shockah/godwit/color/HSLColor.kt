package pl.shockah.godwit.color

import pl.shockah.godwit.geom.Degrees
import kotlin.math.pow
import kotlin.math.sqrt

data class HSLColor(
		var h: Float,
		var s: Float,
		var l: Float
) : ColorSpace<HSLColor> {
	companion object {
		fun from(rgb: RGBColor): HSLColor {
			val max = maxOf(rgb.r, rgb.g, rgb.b)
			val min = minOf(rgb.r, rgb.g, rgb.b)
			val range = max - min

			var h = 0f
			val s: Float
			val l = (max + min) / 2f

			if (range == 0f) {
				s = 0f
			} else {
				s = if (l < 0.5f) range / (max + min) else range / (2f - max - min)

				val rr = ((max - rgb.r) / 6f + range / 2f) / range
				val gg = ((max - rgb.g) / 6f + range / 2f) / range
				val bb = ((max - rgb.b) / 6f + range / 2f) / range

				when (max) {
					rgb.r -> h = bb - gg
					rgb.g -> h = 1f / 3f + rr - bb
					rgb.b -> h = 2f / 3f + gg - rr
				}

				if (h < 0)
					h += 1f
				if (h > 1)
					h -= 1f
			}

			return HSLColor(h, s, l)
		}

		val RGBColor.hsl: HSLColor
			get() = from(this)
	}

	override fun copy(): HSLColor = HSLColor(h, s, l)

	override val rgb: RGBColor
		get() {
			if (s == 0f)
				return RGBColor(l, l, l)

			val v2 = if (l < 0.5f) l * (1 + s) else l + s - s * l
			val v1 = 2 * l - v2

			val r = hue2rgb(v1, v2, h + 1f / 3f)
			val g = hue2rgb(v1, v2, h)
			val b = hue2rgb(v1, v2, h - 1f / 3f)
			return RGBColor(r, g, b)
		}

	private fun hue2rgb(v1: Float, v2: Float, vh: Float): Float {
		var vh2 = vh
		if (vh2 < 0)
			vh2 += 1f
		if (vh2 > 1)
			vh2 -= 1f

		return when {
			6 * vh2 < 1 -> v1 + (v2 - v1) * 6f * vh2
			2 * vh2 < 1 -> v2
			3 * vh2 < 2 -> v1 + (v2 - v1) * (2f / 3f - vh2) * 6f
			else -> v1
		}
	}

	override fun getDistance(other: HSLColor): Float {
		var delta = Degrees(h * 360f) delta Degrees(other.h * 360f)
		if (delta.value < 0)
			delta = Degrees(delta.value + 360f)
		return sqrt((delta.value / 360f).pow(2) + (s - other.s).pow(2) + (l - other.l).pow(2))
	}

	override fun ease(other: HSLColor, f: Float): HSLColor {
		val delta = Degrees(h * 360f) delta Degrees(other.h * 360f)
		val h2 = if (delta.value >= 0) other.h else other.h - 1f
		var h = this.h.ease(h2, f)
		if (h < 0)
			h += 1f
		if (h > 0)
			h -= 1f

		return HSLColor(
				h,
				s.ease(other.s, f),
				l.ease(other.l, f)
		)
	}
}