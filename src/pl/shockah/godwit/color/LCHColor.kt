package pl.shockah.godwit.color

import pl.shockah.godwit.geom.Degrees
import kotlin.math.*

data class LCHColor(
		var l: Float,
		var c: Float,
		var h: Float,
		val reference: XYZColor.Reference = XYZColor.Reference.D65_2
) : ColorSpace<LCHColor> {
	companion object {
		fun from(lab: LabColor): LCHColor {
			var h = atan2(lab.b, lab.a)
			h = (if (h > 0) h / PI * 180 else 360 + h / PI * 180).toFloat()

			return LCHColor(
					lab.l,
					sqrt(lab.a * lab.a + lab.b * lab.b),
					h / 360f,
					lab.reference
			)
		}

		val LabColor.lch: LCHColor
			get() = from(this)
	}

	override val rgb: RGBColor
		get() = lab.rgb

	val exactRgb: RGBColor
		get() = lab.exactRgb

	val lab: LabColor
		get() {
			val h = this.h * 360f
			return LabColor(
					l,
					cos(Math.toRadians(h.toDouble())).toFloat() * c,
					sin(Math.toRadians(h.toDouble())).toFloat() * c,
					reference
			)
		}

	override fun copy(): LCHColor = LCHColor(l, c, h, reference)

	override fun getDistance(other: LCHColor): Float {
		var delta = Degrees(h * 360f) delta Degrees(other.h * 360f)
		if (delta.value < 0)
			delta = Degrees(delta.value + 360f)
		return sqrt((delta.value / 360f).pow(2) + (l - other.l).pow(2) + (c - other.c).pow(2))
	}

	override fun ease(other: LCHColor, f: Float): LCHColor {
		val delta = Degrees(h * 360f) delta Degrees(other.h * 360f)
		val h2 = if (delta.value >= 0) other.h else other.h - 1f
		var h = this.h.ease(h2, f)
		if (h < 0)
			h += 1f
		if (h > 0)
			h -= 1f

		return LCHColor(
				l.ease(other.l, f),
				c.ease(other.c, f),
				h,
				reference
		)
	}
}