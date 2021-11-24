package pl.shockah.aoc

import java.io.File
import kotlin.contracts.ExperimentalContracts
import kotlin.reflect.full.createInstance

@ExperimentalContracts
class AdventValidator {
	companion object {
		@Suppress("UNCHECKED_CAST")
		@JvmStatic
		fun main(args: Array<String>) {
			val year = args[0].toInt()
			val day = args[1].toInt()
			val modifier = if (args.size == 3) args[2] else null
			validate(year, day, modifier)
		}

		fun validate(year: Int, day: Int, modifier: String? = null) {
			try {
				@Suppress("UNCHECKED_CAST")
				val task = Class.forName("pl.shockah.aoc.y$year.Day$day").kotlin.createInstance() as? AdventTask<Any, Any, Any>
				task?.let {
					val inputFileName = "${task.year}/Day${task.day}"
					val inputFullFileName = if (modifier == null) "aocInput/$inputFileName.txt" else "aocInput/$inputFileName-$modifier.txt"

					val parsedInput = task.parseInput(File(inputFullFileName).readText().trimEnd())
					task.part1(parsedInput).toString()

					fun getOutput(part: String): String {
						val outputFileName = "${task.year}/Day${task.day}$part"
						val outputFullFileName = if (modifier == null) "aocOutput/$outputFileName.txt" else "aocOutput/$outputFileName-$modifier.txt"
						return File(outputFullFileName).readText()
					}

					fun validate(part: String, code: (task: AdventTask<Any, Any, Any>, parsedInput: Any) -> Any) {
						val expected = getOutput(part).trim()
						val actual = code(task, parsedInput).toString().trim()
						println(if (expected == actual) ">>> Part $part validated successfully." else ">>> Validation error for part $part. Expected $expected, got $actual.")
					}

					validate("A") { task, input -> task.part1(input) }
					validate("B") { task, input -> task.part2(input) }
				}
			} catch (e: Throwable) {
				e.printStackTrace()
			}
		}
	}
}