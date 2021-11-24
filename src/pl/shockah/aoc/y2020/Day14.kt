package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse2OrNull
import pl.shockah.aoc.parseOrNull
import java.math.BigInteger
import java.util.regex.Pattern

class Day14: AdventTask<List<Day14.Instruction>, BigInteger, BigInteger>(2020, 14) {
	private val maskPattern = Pattern.compile("mask = ([01X]{36})")
	private val memoryPattern = Pattern.compile("mem\\[(\\d+)] = (\\d+)")

	@Suppress("unused")
	enum class MaskBit(
		val symbol: Char
	) {
		X('X'), Zero('0'), One('1');

		companion object {
			val bySymbol = values().associateBy { it.symbol }
		}
	}

	sealed class Instruction {
		data class Mask(
			val bits: List<MaskBit>
		): Instruction()

		data class Memory(
			val address: BigInteger,
			val value: BigInteger
		): Instruction()
	}

	override fun parseInput(rawInput: String): List<Instruction> {
		return rawInput.lines().map { line ->
			run {
				val mask = maskPattern.parseOrNull<String>(line) ?: return@run
				return@map Instruction.Mask(mask.map { MaskBit.bySymbol[it]!! })
			}

			run {
				val parsedMemory = memoryPattern.parse2OrNull<BigInteger, BigInteger>(line) ?: return@run
				val (address, value) = parsedMemory
				return@map  Instruction.Memory(address, value)
			}

			throw IllegalArgumentException("Cannot parse line `$line`")
		}
	}

	override fun part1(input: List<Instruction>): BigInteger {
		var mask = Array(36) { MaskBit.X }.toList()
		val memory = mutableMapOf<BigInteger, BigInteger>()

		for (instruction in input) {
			when (instruction) {
				is Instruction.Mask -> mask = instruction.bits
				is Instruction.Memory -> {
					var maskedValue = instruction.value
					for (index in mask.indices) {
						when (mask[mask.size - index - 1]) {
							MaskBit.X -> { }
							MaskBit.Zero -> maskedValue = maskedValue.clearBit(index)
							MaskBit.One -> maskedValue = maskedValue.setBit(index)
						}
					}
					memory[instruction.address] = maskedValue
				}
			}
		}
		return memory.values.reduce(BigInteger::plus)
	}

	private data class WriteLogEntry(
		val maskedAddress: List<MaskBit>,
		val value: BigInteger
	) {
		data class CommonAddressesResult(
			val count: Int,
			val leftOverAddress: List<MaskBit?>
		)

		fun findCommonAddresses(maskedAddress: List<MaskBit?>): CommonAddressesResult {
			val leftOverAddress = maskedAddress.toMutableList()
			var count = 1
			for (index in maskedAddress.indices) {
				val checked = maskedAddress[index] ?: continue
				val mine = this.maskedAddress[index]

				when (listOf(checked, mine)) {
					listOf(MaskBit.X, MaskBit.X) -> {
						count *= 2
						leftOverAddress[index] = null
					}
					listOf(MaskBit.X, MaskBit.Zero), listOf(MaskBit.Zero, MaskBit.X) -> leftOverAddress[index] = MaskBit.One
					listOf(MaskBit.X, MaskBit.One), listOf(MaskBit.One, MaskBit.X) -> leftOverAddress[index] = MaskBit.Zero
					listOf(MaskBit.Zero, MaskBit.Zero), listOf(MaskBit.One, MaskBit.One) -> continue
					else -> return CommonAddressesResult(0, maskedAddress)
				}
			}
			return CommonAddressesResult(count, leftOverAddress)
		}
	}

	override fun part2(input: List<Instruction>): BigInteger {
		var mask = Array(36) { MaskBit.Zero }.toList()
		val writeLog = mutableListOf<WriteLogEntry>()
		var sum = 0.toBigInteger()

		for (instruction in input) {
			when (instruction) {
				is Instruction.Mask -> mask = instruction.bits
				is Instruction.Memory -> {
					val maskedAddress = mask.indices.map {
						return@map when (val bit = mask[it]) {
							MaskBit.Zero -> if (instruction.address.testBit(mask.size - it - 1)) MaskBit.One else MaskBit.Zero
							MaskBit.One, MaskBit.X -> bit
						}
					}

					var leftOverAddress: List<MaskBit?> = maskedAddress
					for (writeLogEntry in writeLog.reversed()) {
						val result = writeLogEntry.findCommonAddresses(leftOverAddress)
						sum -= result.count.toBigInteger() * writeLogEntry.value
						leftOverAddress = result.leftOverAddress
						if (leftOverAddress.all { it == null })
							break
					}

					val count = 1 shl maskedAddress.count { it == MaskBit.X }
					sum += instruction.value * count.toBigInteger()
					writeLog += WriteLogEntry(maskedAddress, instruction.value)
				}
			}
		}
		return sum
	}

	class Tests {
		private val task = Day14()

		private val rawInputForPart1 = """
			mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
			mem[8] = 11
			mem[7] = 101
			mem[8] = 0
		""".trimIndent()

		private val rawInput1ForPart2 = """
			mask = 000000000000000000000000000000X1001X
			mem[42] = 100
			mask = 00000000000000000000000000000000X0XX
			mem[26] = 1
		""".trimIndent()

		private val rawInput2ForPart2 = """
			mask = 0000000000000000000000000000000XX0XX
			mem[4] = 3
			mask = 0000000000000000000000000000000X0000
			mem[3] = 2
			mask = 000000000000000000000000000000000X0X
			mem[2] = 1
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInputForPart1)
			Assertions.assertEquals(165.toBigInteger(), task.part1(input))
		}

		@Test
		fun part2Input1() {
			val input = task.parseInput(rawInput1ForPart2)
			Assertions.assertEquals(208.toBigInteger(), task.part2(input))
		}

		@Test
		fun part2Input2() {
			val input = task.parseInput(rawInput2ForPart2)
			Assertions.assertEquals(48.toBigInteger(), task.part2(input))
		}
	}
}