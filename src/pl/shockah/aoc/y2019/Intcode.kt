package pl.shockah.aoc.y2019

import pl.shockah.unikorn.Ref
import java.util.*

class Intcode(
		initialMemory: List<Long>,
		var input: LinkedList<Long>?,
		var output: LinkedList<Long>?
) {
	private val instructions = mutableMapOf<Int, Instruction>()
	private val mutableMemory = initialMemory.toMutableList()
	val memory: List<Long> = mutableMemory

	private val pointer = Ref(0)

	var halted = false
		private set

	val awaiting: Boolean
		get() = awaitFlag && input!!.isEmpty()

	private var awaitFlag = false

	fun register(instructions: List<Instruction>) {
		instructions.forEach { this.instructions[it.opcode] = it }
	}

	fun execute() {
		awaitFlag = false
		val console = object: Console {
			override fun halt() {
				halted = true
			}

			override fun push(value: Long) {
				output!!.add(value)
			}

			override fun pop(): Long? {
				return if (input!!.isEmpty()) null else input!!.removeFirst()
			}

			override fun await() {
				awaitFlag = true
			}
		}

		while (!halted && !awaiting) {
			val rawOpcode = memory[pointer.value++]
			val parameters = Parameters((rawOpcode / 100L).toInt())
			val opcode = (rawOpcode % 100L).toInt()
			val instruction = instructions[opcode] ?: throw IllegalStateException("Unknown opcode `$opcode`")
			instruction.execute(pointer, parameters, mutableMemory, console)
		}
	}

	interface Console {
		fun halt()
		fun push(value: Long)
		fun pop(): Long?
		fun await()
	}

	interface Provider {
		fun getIntcode(initialMemory: List<Long>, input: LinkedList<Long>? = null, output: LinkedList<Long>? = null): Intcode
	}

	class Parameters(
			private var modes: Int
	) {
		private fun nextMode(): Int {
			val mode = modes % 10
			modes /= 10
			return mode
		}

		fun read(pointer: Ref<Int>, memory: List<Long>): Long {
			return when (val mode = nextMode()) {
				1 -> memory[pointer.value++]
				else -> memory[getAddress(pointer, mode, memory)]
			}
		}

		fun getAddress(pointer: Ref<Int>, memory: List<Long>): Int {
			return getAddress(pointer, nextMode(), memory)
		}

		private fun getAddress(pointer: Ref<Int>, mode: Int, memory: List<Long>): Int {
			return when (mode) {
				0 -> memory[pointer.value++].toInt()
				1 -> throw IllegalStateException("Unsupported parameter mode `$mode`")
				else -> throw IllegalStateException("Unknown parameter mode `$mode`")
			}
		}
	}

	data class Instruction(
			val opcode: Int,
			val execute: (pointer: Ref<Int>, parameters: Parameters, memory: MutableList<Long>, console: Console) -> Unit
	)

	abstract class AdventTask<A, B>(
			year: Int,
			day: Int,
			val instructions: List<Instruction>
	): pl.shockah.aoc.AdventTask<List<Long>, A, B>(year, day), Provider {
		override fun parseInput(rawInput: String): List<Long> {
			return rawInput.split(",").map { it.toLong() }
		}

		override fun getIntcode(initialMemory: List<Long>, input: LinkedList<Long>?, output: LinkedList<Long>?): Intcode {
			return Intcode(initialMemory, input, output).also { it.register(instructions) }
		}
	}
}