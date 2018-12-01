package pl.shockah.godwit.ease

interface Easable<T> {
	fun ease(other: T, f: Float): T

	fun <T : Easable<T>> T.ease(other: T, easing: Easing, f: Float): T {
		return easing.ease(this, other, f)
	}

	fun Float.ease(other: Float, f: Float): Float {
		return this + f * (other - this)
	}

	fun Float.ease(other: Float, easing: Easing, f: Float): Float {
		return easing.ease(this, other, f)
	}
}