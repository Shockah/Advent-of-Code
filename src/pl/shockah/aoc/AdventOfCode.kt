package pl.shockah.aoc

import kotlin.reflect.full.createInstance

class AdventOfCode {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			try {
				if (args.size != 2)
					return

				val year = args[0].toInt()
				val day = args[1].toInt()
				val task = Class.forName("pl.shockah.aoc.y$year.Day$day").kotlin.createInstance() as? AdventTask<*, *, *>
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