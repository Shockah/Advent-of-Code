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

	@Suppress("EnumEntryName")
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
				index += 2
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
		val possibleOperations = Operation.values().associateWith { mutableSetOf<Int>() }.toMutableMap()

		input.examples.forEach { example ->
			Operation.values().forEach {
				val registers = example.before.copyOf()
				it(example.instruction, registers)
				if (registers.contentEquals(example.after))
					possibleOperations[it]!! += example.instruction.opcode
			}
		}

		while (true) {
			val oldSize = operations.size

			l@ for ((operation, opcodes) in possibleOperations) {
				when (opcodes.size) {
					0 -> throw IllegalStateException("No opcodes for operation ${operation.name}")
					1 -> {
						val opcode = opcodes.first()
						operations[opcode] = operation
						possibleOperations.remove(operation)
						possibleOperations.values.forEach { it.remove(opcode) }
						break@l
					}
					else -> { }
				}
			}

			check(operations.size != oldSize) { "Cannot figure out opcodes for operations ${(Operation.values().toList() - operations.values).joinToString(", ") { it.name }}" }
			if (possibleOperations.isEmpty())
				break
		}

		println("> Operations:")
		println(operations.toSortedMap().map { "${it.key}: ${it.value}" }.joinToString("\n"))

		val registers = intArrayOf(0, 0, 0, 0)
		input.instructions.forEach {
			operations[it.opcode]!!(it.inputA, it.inputB, it.output, registers)
		}
		println("> Final registers: ${registers.toList()}")
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