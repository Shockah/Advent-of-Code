package pl.shockah.aoc.y2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import pl.shockah.aoc.expects
import java.util.*

class Day5: Intcode.AdventTask<Long, Long>(2019, 5, instructions) {
	companion object {
		val pop = Intcode.Instruction(3) { pointer, parameters, memory, console ->
			val backupPointer = pointer.value - 1
			val value = console.pop()
			if (value == null) {
				pointer.value = backupPointer
				console.await()
			} else {
				val address = parameters.getAddress(pointer, memory)
				memory[address] = value
			}
		}

		val push = Intcode.Instruction(4) { pointer, parameters, memory, console ->
			val address = parameters.getAddress(pointer, memory)
			console.push(memory[address])
		}

		val jumpIfTrue = Intcode.Instruction(5) { pointer, parameters, memory, _ ->
			val value = parameters.read(pointer, memory)
			val jumpTo = parameters.read(pointer, memory).toInt()
			if (value != 0L)
				pointer.value = jumpTo
		}

		val jumpIfFalse = Intcode.Instruction(6) { pointer, parameters, memory, _ ->
			val value = parameters.read(pointer, memory)
			val jumpTo = parameters.read(pointer, memory).toInt()
			if (value == 0L)
				pointer.value = jumpTo
		}

		val lessThan = Intcode.Instruction(7) { pointer, parameters, memory, _ ->
			val a = parameters.read(pointer, memory)
			val b = parameters.read(pointer, memory)
			val output = parameters.getAddress(pointer, memory)
			memory[output] = if (a < b) 1 else 0
		}

		val equals = Intcode.Instruction(8) { pointer, parameters, memory, _ ->
			val a = parameters.read(pointer, memory)
			val b = parameters.read(pointer, memory)
			val output = parameters.getAddress(pointer, memory)
			memory[output] = if (a == b) 1 else 0
		}

		val instructions = Day2.instructions + listOf(pop, push, jumpIfTrue, jumpIfFalse, lessThan, equals)
	}

	override fun part1(input: List<Long>): Long {
		val buffer = LinkedList<Long>()
		buffer.add(1)

		val intcode = getIntcode(input, buffer, buffer)
		intcode.execute()
		while (!buffer.isEmpty()) {
			val output = buffer.removeFirst()
			if (output == 0L)
				continue
			if (buffer.isEmpty())
				return output
		}
		throw IllegalStateException()
	}

	override fun part2(input: List<Long>): Long {
		val buffer = LinkedList<Long>()
		buffer.add(5)

		val intcode = getIntcode(input, buffer, buffer)
		intcode.execute()
		return buffer.removeFirst()
	}

	@Nested
	inner class Tests {
		@TestFactory
		fun execute(): Collection<DynamicTest> = createTestCases(
				listOf(1002, 4, 3, 4, 33) expects listOf(1002, 4, 3, 4, 99)
		) { input, expected -> Assertions.assertEquals(expected.map { it.toLong() }, getIntcode(input.map { it.toLong() }).also { it.execute() }.memory) }
	}
}