package pl.shockah.aoc

import java.io.File
import kotlin.reflect.full.createInstance

class AdventOfCode {
	companion object {
		@Suppress("UNCHECKED_CAST")
		@JvmStatic
		fun main(args: Array<String>) {
			try {
				if (args.size !in 2..3)
					return

				val year = args[0].toInt()
				val day = args[1].toInt()
				val modifier = if (args.size == 3) args[2] else null

				val task = Class.forName("pl.shockah.aoc.y$year.Day$day").kotlin.createInstance() as? AdventTask<Any, Any, Any>
				task?.let {
					val fileName = "${task.year}/Day${task.day}"
					val fullFileName = if (modifier == null) "input/$fileName.txt" else "input/$fileName-$modifier.txt"

					var parsedInput: Any? = null
					measure("Parsing") { parsedInput = task.parseInput(File(fullFileName).readText().trimEnd()) }

					runTask("A") { task.part1(parsedInput!!) }
					runTask("B") { task.part2(parsedInput!!) }
				}
			} catch (e: Throwable) {
				e.printStackTrace()
			}
		}

		private fun measure(name: String, task: () -> Unit) {
			try {
				val before = System.currentTimeMillis()
				task()
				val after = System.currentTimeMillis()
				println("Time $name: ${(after - before) / 1000.0}s")
			} catch (e: Throwable) {
				e.printStackTrace()
			}
		}

		private fun <R> runTask(name: String, task: () -> R) {
			measure(name) {
				println("Result $name: ${task()}")
			}
		}
	}
}