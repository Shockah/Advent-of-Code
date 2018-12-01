package pl.shockah.aoc

import kotlin.reflect.full.primaryConstructor

class AdventOfCode {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			try {
				if (args.size !in 2..3)
					return

				val year = args[0].toInt()
				val day = args[1].toInt()
				val example = if (args.size == 3) args[2].toBoolean() else false

				val task = Class.forName("pl.shockah.aoc.y$year.Day$day").kotlin.primaryConstructor?.call(example) as? AdventTask<*, *, *>
				task?.let {
					measure("Parsing") { task.parsedInput }
					runTask("A", task::part1)
					runTask("B", task::part2)
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