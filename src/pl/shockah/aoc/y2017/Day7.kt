package pl.shockah.aoc.y2017

import pl.shockah.aoc.AdventTask
import java.util.*
import java.util.regex.Pattern

class Day7: AdventTask<List<Day7.Program>, String, Int>(2017, 7) {
	private val inputPattern: Pattern = Pattern.compile("(\\w+) \\((\\d+)\\)(?: -> ([\\w, ]+))?")

	private data class InputEntry(
			val name: String,
			val weight: Int,
			val childrenNames: List<String>
	)

	data class Program(
			val name: String,
			val weight: Int
	) {
		var parent: Program? = null
		val children: MutableList<Program> = mutableListOf()

		val totalWeight: Int by lazy {
			weight + children.map { it.totalWeight }.sum()
		}
	}

	override fun parseInput(rawInput: String): List<Program> {
		val entries = LinkedList(rawInput.lines().map {
			val matcher = inputPattern.matcher(it)
			if (!matcher.find())
				throw IllegalArgumentException()

			val name = matcher.group(1)
			val weight = matcher.group(2).toInt()

			val rawChildren = matcher.group(3)
			val children = rawChildren?.split(",")?.map { it.trim() } ?: listOf()

			return@map InputEntry(name, weight, children)
		})

		val programs = mutableMapOf<String, Program>()
		while (!entries.isEmpty()) {
			val oldCount = entries.size
			val iterator = entries.iterator()
			outer@ while (iterator.hasNext()) {
				val entry = iterator.next()

				val children = mutableListOf<Program>()
				for (childName in entry.childrenNames) {
					children += programs[childName] ?: continue@outer
				}

				val program = Program(entry.name, entry.weight)
				program.children += children
				programs[program.name] = program
				iterator.remove()

				for (child in children) {
					child.parent = program
				}
			}

			if (oldCount == entries.size)
				throw IllegalArgumentException("Cycle between programs.")
		}

		return programs.values.toList()
	}

	private fun getRoot(input: List<Program>): Program {
		var current: Program = input[0]
		while (true) {
			current = current.parent ?: return current
		}
	}

	override fun part1(input: List<Program>): String {
		return getRoot(input).name
	}

	private val Program.unbalancedProgram: Program?
		get() {
			for (child in children) {
				child.unbalancedProgram?.let {
					return it
				}
			}

			if (children.isEmpty())
				return null
			val groups = children.groupBy { it.totalWeight }.values.sortedBy { it.size }
			return if (groups.size == 1) null else groups[0][0]
		}

	override fun part2(input: List<Program>): Int {
		val unbalanced = getRoot(input).unbalancedProgram ?: throw IllegalStateException("No unbalanced program.")
		val siblingWeight = unbalanced.parent?.children?.first { it != unbalanced }?.totalWeight ?: throw IllegalArgumentException()
		return unbalanced.weight + (siblingWeight - unbalanced.totalWeight)
	}
}