package pl.shockah.aoc.y2019

import pl.shockah.unikorn.Ref
import java.util.*

class Intcode(
		initialMemory: List<Int>,
		var input: LinkedList<Int>?,
		var output: LinkedList<Int>?
) {
	private val instructions = mutableMapOf<Int, Instruction>()
	private val mutableMemory = initialMemory.toMutableList()
	val memory: List<Int> = mutableMemory

	fun register(vararg instructions: Instruction) {
		register(instructions.toList())
	}

	fun register(instructions: List<Instruction>) {
		instructions.forEach { this.instructions[it.opcode] = it }
	}

	fun execute() {
		var shouldHalt = false
		val pointer = Ref(0)

		val console = object: Console {
			override fun halt() {
				shouldHalt = true
			}

			override fun push(value: Int) {
				output!!.add(value)
			}

			override fun pop(): Int {
				return input!!.removeFirst()
			}
		}

		while (!shouldHalt) {
			val rawOpcode = memory[pointer.value++]
			val parameters = Parameters(rawOpcode / 100)
			val opcode = rawOpcode % 100
			val instruction = instructions[opcode] ?: throw IllegalStateException("Unknown opcode `$opcode`")
			instruction.execute(pointer, parameters, mutableMemory, console)
		}
	}

	interface Console {
		fun halt()
		fun push(value: Int)
		fun pop(): Int
	}

	interface Provider {
		fun getIntcode(initialMemory: List<Int>, input: LinkedList<Int>? = null, output: LinkedList<Int>? = null): Intcode
	}

	class Parameters(
			private var modes: Int
	) {
		private fun nextMode(): Int {
			val mode = modes % 10
			modes /= 10
			return mode
		}

		fun read(pointer: Ref<Int>, memory: List<Int>): Int {
			return when (val mode = nextMode()) {
				0 -> memory[memory[pointer.value++]]
				1 -> memory[pointer.value++]
				else -> throw IllegalStateException("Unknown parameter mode `$mode`")
			}
		}
	}

	data class Instruction(
			val opcode: Int,
			val execute: (pointer: Ref<Int>, parameters: Parameters, memory: MutableList<Int>, console: Console) -> Unit
	) {

	}
}