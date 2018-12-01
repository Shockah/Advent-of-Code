package pl.shockah.godwit

import com.badlogic.gdx.ApplicationAdapter

class Godwit(
		private val initialStateFactory: () -> GameState
) : ApplicationAdapter() {
	private var _state: GameState? = null
	var state: GameState
		get() = _state ?: throw IllegalStateException("Not yet initialized.")
		private set(value) {
			_state = value
		}

	override fun create() {
		state = initialStateFactory()
	}

	override fun render() {
		state.render()
	}
}