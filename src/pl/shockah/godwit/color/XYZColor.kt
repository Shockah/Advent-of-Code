package pl.shockah.godwit.color

import kotlin.math.pow
import kotlin.math.sqrt

data class XYZColor(
		var x: Float,
		var y: Float,
		var z: Float
) : ColorSpace<XYZColor> {
	data class Reference(
			val x: Float,
			val y: Float,
			val z: Float
	) {
		companion object {
			val D50_2: Reference = Reference(96.422f, 100f, 82.521f)
			val D50_10: Reference = Reference(96.720f, 100f, 81.427f)

			val D65_2: Reference = Reference(95.047f, 100f, 108.883f)
			val D65_10: Reference = Reference(94.811f, 100f, 107.304f)
		}
	}

	companion object {
		fun from(rgb: RGBColor): XYZColor {
			var r = if (rgb.r > 0.04045f) ((rgb.r + 0.055f) / 1.055f).pow(2.4f) else rgb.r / 12.92f
			var g = if (rgb.g > 0.04045f) ((rgb.g + 0.055f) / 1.055f).pow(2.4f) else rgb.g / 12.92f
			var b = if (rgb.b > 0.04045f) ((rgb.b + 0.055f) / 1.055f).pow(2.4f) else rgb.b / 12.92f

			r *= 100
			g *= 100
			b *= 100

			return XYZColor(
					r * 0.4124f + g * 0.3576f + b * 0.1805f,
					r * 0.2126f + g * 0.7152f + b * 0.0722f,
					r * 0.0193f + g * 0.1192f + b * 0.9505f
			)
		}

		val RGBColor.xyz: XYZColor
			get() = from(this)
	}

	override val rgb: RGBColor
		get() = internalRGB(true, false)

	val exactRgb: RGBColor
		get() = internalRGB(false, true)

	override fun copy(): XYZColor = XYZColor(x, y, z)

	private fun internalRGB(clamp: Boolean, rangeException: Boolean): RGBColor {
		val x = this.x / 100
		val y = this.y / 100
		val z = this.z / 100

		var r = x * 3.2406f - y * 1.5372f - z * 0.4986f
		var g = -x * 0.9689f + y * 1.8758f + z * 0.0415f
		var b = x * 0.0557f - y * 0.2040f + z * 1.0570f

		r = if (r > 0.0031308f) 1.055f * r.pow(1f / 2.4f) - 0.055f else r * 12.92f
		g = if (g > 0.0031308f) 1.055f * g.pow(1f / 2.4f) - 0.055f else g * 12.92f
		b = if (b > 0.0031308f) 1.055f * b.pow(1f / 2.4f) - 0.055f else b * 12.92f

		if (clamp) {
			r = r.coerceIn(0f, 1f)
			g = g.coerceIn(0f, 1f)
			b = b.coerceIn(0f, 1f)
		} else if (rangeException) {
			if (r < 0f || r > 1f)
				throw IllegalArgumentException("Cannot convert to RGB - R outside the 0-1 bounds.")
			if (g < 0f || g > 1f)
				throw IllegalArgumentException("Cannot convert to RGB - G outside the 0-1 bounds.")
			if (b < 0f || b > 1f)
				throw IllegalArgumentException("Cannot convert to RGB - B outside the 0-1 bounds.")
		}

		return RGBColor(r, g, b)
	}

	override fun getDistance(other: XYZColor): Float {
		return sqrt((x - other.x).pow(2) * 0.01f + (y - other.y).pow(2) * 0.01f + (z - other.z).pow(2) * 0.01f)
	}

	override fun ease(other: XYZColor, f: Float): XYZColor {
		return XYZColor(
				x.ease(other.x, f),
				y.ease(other.y, f),
				z.ease(other.z, f)
		)
	}
}