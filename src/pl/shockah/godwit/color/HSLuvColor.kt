package pl.shockah.godwit.color

import pl.shockah.godwit.geom.Degrees
import kotlin.math.*

data class HSLuvColor(
		var h: Float,
		var s: Float,
		var luv: Float,
		val reference: XYZColor.Reference = XYZColor.Reference.D65_2
) : ColorSpace<HSLuvColor> {
	companion object {
		private const val kappa = 903.2962962f
		private const val epsilon = 0.0088564516f

		private val m = arrayOf(
				floatArrayOf(3.240969941904521f, -1.537383177570093f, -0.498610760293f),
				floatArrayOf(-0.96924363628087f, 1.87596750150772f, 0.041555057407175f),
				floatArrayOf(0.055630079696993f, -0.20397695888897f, 1.056971514242878f)
		)

		fun from(lch: LCHColor): HSLuvColor {
			if (lch.l > 99.9999999f)
				return HSLuvColor(lch.h, 0f, 1f, lch.reference)
			if (lch.l < 0.00000001f)
				return HSLuvColor(lch.h, 0f, 0f, lch.reference)

			val max = maxChromaForLH(lch.l, lch.h)
			val s = lch.c / max
			return HSLuvColor(lch.h, s, lch.l * 0.01f, lch.reference)
		}

		val LCHColor.hsluv: HSLuvColor
			get() = from(this)

		private fun maxChromaForLH(L: Float, H: Float): Float {
			val hrad = (H / 360f) * PI * 2
			val bounds = getBounds(L)
			var min = Float.MAX_VALUE

			for (bound in bounds) {
				val length = lengthOfRayUntilIntersect(hrad, bound)
				if (length >= 0f)
					min = Math.min(min, length)
			}

			return min
		}

		private fun lengthOfRayUntilIntersect(theta: Double, line: FloatArray): Float {
			return (line[1] / (sin(theta) - line[0] * cos(theta))).toFloat()
		}

		private fun getBounds(L: Float): List<FloatArray> {
			val result: MutableList<FloatArray> = mutableListOf()

			val sub1 = (L + 16).pow(3) / 1560896
			val sub2 = if (sub1 > epsilon) sub1 else L / kappa

			for (c in 0..2) {
				val m1 = m[c][0]
				val m2 = m[c][1]
				val m3 = m[c][2]

				for (t in 0..1) {
					val top1 = (284517 * m1 - 94839 * m3) * sub2
					val top2 = (838422 * m3 + 769860 * m2 + 731718 * m1) * L * sub2 - 769860f * t.toFloat() * L
					val bottom = (632260 * m3 - 126452 * m2) * sub2 + 126452 * t
					result.add(floatArrayOf(top1 / bottom, top2 / bottom))
				}
			}

			return result
		}
	}

	override val rgb: RGBColor
		get() = lch.rgb

	val exactRgb: RGBColor
		get() = lch.exactRgb

	val lch: LCHColor
		get() {
			if (luv > 0.999999999f)
				return LCHColor(1f, 0f, h)
			if (luv < 0.0000000001f)
				return LCHColor(0f, 0f, h)

			val max = maxChromaForLH(luv * 100f, h)
			val c = max * s
			return LCHColor(luv * 100f, c, h, reference)
		}

	override fun copy(): HSLuvColor = HSLuvColor(h, s, luv, reference)

	override fun getDistance(other: HSLuvColor): Float {
		var delta = Degrees(h * 360f) delta Degrees(other.h * 360f)
		if (delta.value < 0)
			delta = Degrees(delta.value + 360f)
		return sqrt((delta.value / 360f).pow(2) + (s - other.s).pow(2) + (luv - other.luv).pow(2))
	}

	override fun ease(other: HSLuvColor, f: Float): HSLuvColor {
		val delta = Degrees(h * 360f) delta Degrees(other.h * 360f)
		val h2 = if (delta.value >= 0) other.h else other.h - 1f
		var h = this.h.ease(h2, f)
		if (h < 0)
			h += 1f
		if (h > 0)
			h -= 1f

		return HSLuvColor(
				h,
				s.ease(other.s, f),
				luv.ease(other.luv, f),
				reference
		)
	}
}