package pl.shockah.aoc.y2015

import pl.shockah.aoc.AdventTask
import kotlin.math.max

class Day22 : AdventTask<Day22.Stats, Int, Int>(2015, 22) {
	data class Stats(
			val health: Int,
			val mana: Int,
			val damage: Int = 0,
			val armor: Int = 0
	)

	private class Character(
			val stats: Stats
	) {
		var health = stats.health
		var mana = stats.mana
		var armor = stats.armor
		var effectTimers = mutableMapOf<Effect, Int>()

		fun attack(character: Character): Boolean {
			val actualDamage = max(stats.damage - character.stats.armor, 1)
			character.health -= actualDamage
			return character.health <= 0
		}

		fun hasEffect(effect: Effect): Boolean {
			return (effectTimers[effect] ?: 0) > 0
		}

		fun tickEffects() {
			for (kvp in effectTimers) {
				if (kvp.value <= 0)
					continue

				kvp.key.onTick?.invoke(this)
				kvp.setValue(kvp.value - 1)
			}
		}

		fun copy(): Character {
			val character = Character(stats)
			character.health = health
			character.mana = mana
			character.armor = armor
			character.effectTimers = effectTimers.toMutableMap()
			return character
		}
	}

	private enum class Effect(
			val duration: Int,
			val onTick: ((character: Character) -> Unit)? = null,
			val onApply: ((character: Character) -> Unit)? = null,
			val onRemove: ((character: Character) -> Unit)? = null
	) {
		Shield(6, onApply = { it.armor += 7 }, onRemove = { it.armor -= 7 }),
		Poison(6, onTick = { it.health -= 3 }),
		Recharge(5, onTick = { it.mana += 101 })
	}

	private enum class EffectTarget {
		Caster, Enemy
	}

	private data class AppliedEffect(
			val effect: Effect,
			val target: EffectTarget
	) {
		fun getRealTarget(caster: Character, enemy: Character): Character {
			return when (target) {
				EffectTarget.Caster -> caster
				EffectTarget.Enemy -> enemy
			}
		}

		fun apply(caster: Character, enemy: Character) {
			val target = getRealTarget(caster, enemy)
			target.effectTimers[effect] = effect.duration
			effect.onApply?.invoke(target)
		}
	}

	private enum class Spell(
			val cost: Int,
			val appliedEffect: AppliedEffect? = null,
			val action: ((caster: Character, enemy: Character) -> Unit)? = null
	) {
		MagicMissile(53, action = { _, enemy -> enemy.health -= 4 }),
		Drain(73, action = { caster, enemy ->
			enemy.health -= 2
			caster.health += 2
		}),
		Shield(113, AppliedEffect(Effect.Shield, EffectTarget.Caster)),
		Poison(173, AppliedEffect(Effect.Poison, EffectTarget.Enemy)),
		Recharge(229, AppliedEffect(Effect.Recharge, EffectTarget.Caster));

		fun cast(caster: Character, enemy: Character) {
			caster.mana -= cost
			appliedEffect?.apply(caster, enemy)
			action?.invoke(caster, enemy)
		}

		fun isCastable(caster: Character, enemy: Character): Boolean {
			if (caster.mana < cost)
				return false
			if (appliedEffect != null && appliedEffect.getRealTarget(caster, enemy).hasEffect(appliedEffect.effect))
				return false
			return true
		}
	}

	private sealed class Action {
		data class CasterCast(
				val spell: Spell
		) : Action()

		object EnemyAttack : Action()

		object EffectTick : Action()
	}

	private data class State(
			val caster: Character,
			val enemy: Character,
			val actions: List<Action>
	) {
		fun getResult(): Result {
			return Result(
					caster.health,
					enemy.health,
					actions.count { it != Action.EffectTick },
					actions.filterIsInstance<Action.CasterCast>().sumBy { it.spell.cost }
			)
		}
	}

	private data class Result(
			val playerHealth: Int,
			val enemyHealth: Int,
			val turns: Int,
			val manaSpent: Int
	) {
		override fun toString(): String {
			return if (playerHealth <= 0)
				"Died after $turns turns after spending $manaSpent mana. Health left: $enemyHealth"
			else
				"Victorious after $turns turns after spending $manaSpent mana. Health left: $playerHealth"
		}
	}

	override fun parseInput(rawInput: String): Stats {
		val lines = rawInput.lines()
		return Stats(
				lines[0].split(" ").last().toInt(),
				0,
				lines[1].split(" ").last().toInt()
		)
	}

	private fun simulate(caster: Character, enemy: Character): Set<State> {
		return simulatePlayerTurn(State(caster, enemy, emptyList())).filter { it.caster.health <= 0 || it.enemy.health <= 0 }.toSet()
	}

	private fun simulateEffectTick(state: State): State {
		if (state.caster.health <= 0 || state.enemy.health <= 0)
			return state

		val caster = state.caster.copy()
		val enemy = state.enemy.copy()

		caster.tickEffects()
		enemy.tickEffects()
		return State(caster, enemy, state.actions + Action.EffectTick)
	}

	private fun simulatePlayerTurn(state: State): Set<State> {
		val newState = simulateEffectTick(state)
		if (newState.caster.health <= 0 || newState.enemy.health <= 0)
			return setOf(newState)

		val availableSpells = Spell.values().filter { it.isCastable(newState.caster, newState.enemy) }
		if (availableSpells.isEmpty())
			return emptySet()
		return availableSpells.flatMap {
			val caster = newState.caster.copy()
			val enemy = newState.enemy.copy()
			it.cast(caster, enemy)
			return@flatMap simulateEnemyTurn(State(caster, enemy, newState.actions + Action.CasterCast(it)))
		}.toSet()
	}

	private fun simulateEnemyTurn(state: State): Set<State> {
		val newState = simulateEffectTick(state)
		if (newState.caster.health <= 0 || newState.enemy.health <= 0)
			return setOf(newState)

		val caster = newState.caster.copy()
		newState.enemy.attack(caster)
		return simulatePlayerTurn(State(caster, newState.enemy, newState.actions + Action.EnemyAttack))
	}

	override fun part1(input: Stats): Int {
		val states = simulate(Character(Stats(50, 500)), Character(input))

		for (result in states.map { it.getResult() }) {
			println(result)
		}

		TODO()
	}

	override fun part2(input: Stats): Int {
		TODO()
	}
}