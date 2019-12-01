package pl.shockah.aoc.y2015

import pl.shockah.aoc.AdventTask
import pl.shockah.unikorn.nextInCycle
import java.util.*
import kotlin.math.max

class Day22: AdventTask<Day22.Stats, Int, Int>(2015, 22) {
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
			val actualDamage = max(stats.damage - character.armor, 1)
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
				if (kvp.value <= 0)
					kvp.key.onRemove?.invoke(this)
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
		): Action() {
			override fun toString(): String {
				return "Cast: ${spell.name}"
			}
		}

		object CasterNoSpellAvailable: Action() {
			override fun toString(): String {
				return "No Spell Available"
			}
		}

		object EnemyAttack: Action() {
			override fun toString(): String {
				return "Enemy Attack"
			}
		}

		object EffectTick: Action() {
			override fun toString(): String {
				return "Effect Tick"
			}
		}
	}

	private enum class Turn {
		HardDifficultyDamage, FirstTick, Caster, SecondTick, Enemy
	}

	private data class State(
			val turn: Turn,
			val caster: Character,
			val enemy: Character,
			val actions: List<Action>
	) {
		val spells: List<Spell>
			get() = actions.filterIsInstance<Action.CasterCast>().map { it.spell }

		val manaSpent: Int
			get() = spells.sumBy { it.cost }

		constructor(
				caster: Character,
				enemy: Character
		): this(Turn.FirstTick, caster, enemy, emptyList())

		fun getResult(): Result {
			return Result(
					caster.health,
					enemy.health,
					actions.count { it != Action.EffectTick },
					actions.filterIsInstance<Action.CasterCast>().sumBy { it.spell.cost },
					when {
						caster.health <= 0 -> FightResult.Loss
						enemy.health <= 0 -> FightResult.Win
						actions.last() == Action.CasterNoSpellAvailable -> FightResult.NoSpellAvailable
						else -> throw IllegalStateException()
					}
			)
		}
	}

	private enum class FightResult {
		Win, Loss, NoSpellAvailable
	}

	private enum class Difficulty(
			val ignoreTurns: Set<Turn> = emptySet()
	) {
		Normal(setOf(Turn.HardDifficultyDamage)), Hard
	}

	private data class Result(
			val playerHealth: Int,
			val enemyHealth: Int,
			val turns: Int,
			val manaSpent: Int,
			val fightResult: FightResult
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

	@Suppress("NAME_SHADOWING")
	private fun task(caster: Character, enemy: Character, difficulty: Difficulty): State {
		val games = LinkedList<State>()
		games += State(caster, enemy)

		var bestGame: State? = null

		while (!games.isEmpty()) {
			val game = games.removeFirst()
			if (bestGame != null && game.manaSpent >= bestGame.manaSpent)
				continue

			if (game.caster.health <= 0)
				continue

			if (game.enemy.health <= 0) {
				if (bestGame == null || game.manaSpent < bestGame.manaSpent)
					bestGame = game
				continue
			}

			if (game.turn in difficulty.ignoreTurns) {
				games += State(game.turn.nextInCycle, game.caster, game.enemy, game.actions)
				continue
			}

			when (game.turn) {
				Turn.HardDifficultyDamage -> {
					val caster = game.caster.copy().apply { health-- }
					games += State(game.turn.nextInCycle, caster, game.enemy, game.actions + Action.EffectTick)
				}
				Turn.FirstTick, Turn.SecondTick -> {
					val caster = game.caster.copy().apply { tickEffects() }
					val enemy = game.enemy.copy().apply { tickEffects() }
					games += State(game.turn.nextInCycle, caster, enemy, game.actions + Action.EffectTick)
				}
				Turn.Caster -> {
					val availableSpells = Spell.values().filter { it.isCastable(game.caster, game.enemy) }
					games += availableSpells.map {
						val caster = game.caster.copy()
						val enemy = game.enemy.copy()
						it.cast(caster, enemy)
						return@map State(game.turn.nextInCycle, caster, enemy, game.actions + Action.CasterCast(it))
					}
				}
				Turn.Enemy -> {
					val caster = game.caster.copy()
					val enemy = game.enemy.copy()
					enemy.attack(caster)
					games += State(game.turn.nextInCycle, caster, enemy, game.actions + Action.EnemyAttack)
				}
			}
		}

		return bestGame ?: throw IllegalStateException()
	}

	override fun part1(input: Stats): Int {
		val game = task(Character(Stats(50, 500)), Character(input), Difficulty.Normal)
		println(game.spells.joinToString())
		println("\t${game.getResult()}")
		return game.manaSpent
	}

	override fun part2(input: Stats): Int {
		val game = task(Character(Stats(50, 500)), Character(input), Difficulty.Hard)
		println(game.spells.joinToString())
		println("\t${game.getResult()}")
		return game.manaSpent
	}
}