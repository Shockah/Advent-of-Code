package pl.shockah.aoc.y2018

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse4
import java.util.regex.Pattern

class Day16: AdventTask<Day16.Input, Int, Int>(2018, 16) {
	private val examplePattern = Pattern.compile(".*?\\[(\\d+), (\\d+), (\\d+), (\\d+)]")

	data class Input(
			val examples: List<Example>,
			val instructions: List<Instruction>
	)

	data class Example(
			val before: IntArray,
			val instruction: Instruction,
			val after: IntArray
	) {
		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (javaClass != other?.javaClass) return false

			other as Example

			if (!before.contentEquals(other.before)) return false
			if (instruction != other.instruction) return false
			if (!after.contentEquals(other.after)) return false

			return true
		}

		override fun hashCode(): Int {
			var result = before.contentHashCode()
			result = 31 * result + instruction.hashCode()
			result = 31 * result + after.contentHashCode()
			return result
		}
	}

	data class Instruction(
			val opcode: Int,
			val inputA: Int,
			val inputB: Int,
			val output: Int
	)

	enum class Operation(
			private val func: (a: Int, b: Int, out: Int, registers: IntArray) -> Unit
	) {
		addr({ a, b, out, registers -> registers[out] = registers[a] + registers[b] }),
		addi({ a, b, out, registers -> registers[out] = registers[a] + b }),
		mulr({ a, b, out, registers -> registers[out] = registers[a] * registers[b] }),
		muli({ a, b, out, registers -> registers[out] = registers[a] * b }),
		banr({ a, b, out, registers -> registers[out] = registers[a] and registers[b] }),
		bani({ a, b, out, registers -> registers[out] = registers[a] and b }),
		borr({ a, b, out, registers -> registers[out] = registers[a] or registers[b] }),
		bori({ a, b, out, registers -> registers[out] = registers[a] or b }),
		setr({ a, _, out, registers -> registers[out] = registers[a] }),
		seti({ a, _, out, registers -> registers[out] = a }),
		gtir({ a, b, out, registers -> registers[out] = if (a > registers[b]) 1 else 0 }),
		gtri({ a, b, out, registers -> registers[out] = if (registers[a] > b) 1 else 0 }),
		gtrr({ a, b, out, registers -> registers[out] = if (registers[a] > registers[b]) 1 else 0 }),
		eqir({ a, b, out, registers -> registers[out] = if (a == registers[b]) 1 else 0 }),
		eqri({ a, b, out, registers -> registers[out] = if (registers[a] == b) 1 else 0 }),
		eqrr({ a, b, out, registers -> registers[out] = if (registers[a] == registers[b]) 1 else 0 });

		operator fun invoke(a: Int, b: Int, out: Int, registers: IntArray) {
			func(a, b, out, registers)
		}

		operator fun invoke(instruction: Instruction, registers: IntArray) {
			func(instruction.inputA, instruction.inputB, instruction.output, registers)
		}

		companion object {
			val byName = values().map { it.name to it }.toMap()
		}
	}

	override fun parseInput(rawInput: String): Input {
		val lines = rawInput.lines()
		var index = 0
		val examples = mutableListOf<Example>()
		val instructions = mutableListOf<Instruction>()

		while (index < lines.size) {
			try {
				val before = parseRegisters(lines[index])
				val instruction = parseInstruction(lines[index + 1])
				val after = parseRegisters(lines[index + 2])
				examples += Example(before, instruction, after)
				index += 4
			} catch (ignored: IllegalArgumentException) {
				index += 6
				break
			}
		}

		for (i in index until lines.size) {
			instructions += parseInstruction(lines[i])
		}
		return Input(examples, instructions)
	}

	private fun parseRegisters(line: String): IntArray {
		val (a, b, c, d) = examplePattern.parse4<Int, Int, Int, Int>(line)
		return intArrayOf(a, b, c, d)
	}

	private fun parseInstruction(line: String): Instruction {
		val split = line.split(" ").map { it.trim().toInt() }
		return Instruction(split[0], split[1], split[2], split[3])
	}

	override fun part1(input: Input): Int {
		return input.examples.count { example ->
			return@count Operation.values().count {
				val registers = example.before.copyOf()
				it(example.instruction, registers)
				registers.contentEquals(example.after)
			} >= 3
		}
	}

	override fun part2(input: Input): Int {
		val operations = mutableMapOf<Int, Operation>()
		val potentialOperationsMap = (0 until Operation.values().size).map { it to Operation.values().toSet().toSet() }.toMap().toMutableMap()

		fun truncateOperations() {
			outer@ while (true) {
				for ((opcode, potentialOperations) in potentialOperationsMap) {
					if (potentialOperations.size == 1) {
						val operation = potentialOperations.first()
						operations[opcode] = operation
						potentialOperationsMap.remove(opcode)

						for ((opcode, potentialOperations) in potentialOperationsMap) {
							potentialOperationsMap[opcode] = potentialOperations - operation
						}

						continue@outer
					}
				}
				return
			}
		}

		for (example in input.examples) {
			val potentialOperations = potentialOperationsMap[example.instruction.opcode] ?: continue

			potentialOperationsMap[example.instruction.opcode] = potentialOperations.filter {
				val registers = example.before.copyOf()
				it(example.instruction, registers)
				return@filter registers.contentEquals(example.after)
			}.toSet()

			truncateOperations()
		}

		for ((opcode, potentialOperations) in potentialOperationsMap) {
			if (potentialOperations.size != 1)
				throw IllegalStateException("Too many potential operations ($potentialOperations) for opcode $opcode")
		}

		val registers = intArrayOf(0, 0, 0, 0)
		for (instruction in input.instructions) {
			operations[instruction.opcode]!!(instruction, registers)
		}
		return registers[0]
	}

	class Tests {
		private val task = Day16()

		@Test
		fun part1() {
			val input = task.parseInput("""
				Before: [3, 2, 1, 1]
				9 2 1 2
				After:  [3, 2, 2, 1]



				9 2 1 2
			""".trimIndent())
			Assertions.assertEquals(1, task.part1(input))
		}
	}
}