package pl.shockah.godwit.ease

open class SmoothstepEasing : Easing() {
	companion object {
		val one = SmoothstepEasing()
		val two = object : SmoothstepEasing() {
			override fun ease(f: Float): Float {
				val f2 = super.ease(f)
				return f2 * f2
			}
		}
		val three = object : SmoothstepEasing() {
			override fun ease(f: Float): Float {
				val f2 = super.ease(f)
				return f2 * f2 * f2
			}
		}
	}

	override fun ease(f: Float): Float {
		return f * f * (3f - 2f * f);
	}
}