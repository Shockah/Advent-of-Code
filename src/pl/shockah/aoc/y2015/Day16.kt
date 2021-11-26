package pl.shockah.aoc.y2015

import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parse2
import java.util.regex.Pattern

class Day16: AdventTask<List<Day16.Aunt>, Int, Int>(2015, 16) {
	private val baseInputPattern: Pattern = Pattern.compile("Sue (\\d+): (.*)")
	private val thingPattern: Pattern = Pattern.compile("(\\w+): (\\d+)")

	private val lookingForPart1 = AuntInfo(
		children = 3,
		cars = 7,
		samoyeds = 2,
		pomeranians = 3,
		akitas = 0,
		vizslas = 0,
		goldfish = 5,
		trees = 3,
		cats = 2,
		perfumes = 1
	)

	private val lookingForPart2 = AuntLookup(
		children = 3..3,
		cars = 7..7,
		samoyeds = 2..2,
		pomeranians = 0 until 3,
		akitas = 0..0,
		vizslas = 0..0,
		goldfish = 0 until 5,
		trees = 4..Int.MAX_VALUE,
		cats = 3..Int.MAX_VALUE,
		perfumes = 1..1
	)

	data class Aunt(
		val number: Int,
		val info: AuntInfo
	)

	data class AuntLookup(
		val children: IntRange,
		val cats: IntRange,
		val samoyeds: IntRange,
		val pomeranians: IntRange,
		val akitas: IntRange,
		val vizslas: IntRange,
		val goldfish: IntRange,
		val trees: IntRange,
		val cars: IntRange,
		val perfumes: IntRange
	)

	data class AuntInfo(
		val children: Int? = null,
		val cats: Int? = null,
		val samoyeds: Int? = null,
		val pomeranians: Int? = null,
		val akitas: Int? = null,
		val vizslas: Int? = null,
		val goldfish: Int? = null,
		val trees: Int? = null,
		val cars: Int? = null,
		val perfumes: Int? = null
	) {
		infix fun matches(info: AuntInfo): Boolean {
			if (children != null && info.children != null && children != info.children)
				return false
			if (cats != null && info.cats != null && cats != info.cats)
				return false
			if (samoyeds != null && info.samoyeds != null && samoyeds != info.samoyeds)
				return false
			if (pomeranians != null && info.pomeranians != null && pomeranians != info.pomeranians)
				return false
			if (akitas != null && info.akitas != null && akitas != info.akitas)
				return false
			if (vizslas != null && info.vizslas != null && vizslas != info.vizslas)
				return false
			if (goldfish != null && info.goldfish != null && goldfish != info.goldfish)
				return false
			if (trees != null && info.trees != null && trees != info.trees)
				return false
			if (cars != null && info.cars != null && cars != info.cars)
				return false
			if (perfumes != null && info.perfumes != null && perfumes != info.perfumes)
				return false
			return true
		}

		infix fun matches(info: AuntLookup): Boolean {
			if (children != null && children !in info.children)
				return false
			if (cats != null && cats !in info.cats)
				return false
			if (samoyeds != null && samoyeds !in info.samoyeds)
				return false
			if (pomeranians != null && pomeranians !in info.pomeranians)
				return false
			if (akitas != null && akitas !in info.akitas)
				return false
			if (vizslas != null && vizslas !in info.vizslas)
				return false
			if (goldfish != null && goldfish !in info.goldfish)
				return false
			if (trees != null && trees !in info.trees)
				return false
			if (cars != null && cars !in info.cars)
				return false
			if (perfumes != null && perfumes !in info.perfumes)
				return false
			return true
		}
	}

	override fun parseInput(rawInput: String): List<Aunt> {
		return rawInput.lines().map {
			val (number, rawThings) = baseInputPattern.parse2<Int, String>(it)

			var info = AuntInfo()
			val matcher = thingPattern.matcher(rawThings)
			while (matcher.find()) {
				val amount = matcher.group(2).toInt()
				when (matcher.group(1)) {
					"children" -> info = info.copy(children = amount)
					"cats" -> info = info.copy(cats = amount)
					"samoyeds" -> info = info.copy(samoyeds = amount)
					"pomeranians" -> info = info.copy(pomeranians = amount)
					"akitas" -> info = info.copy(akitas = amount)
					"vizslas" -> info = info.copy(vizslas = amount)
					"goldfish" -> info = info.copy(goldfish = amount)
					"trees" -> info = info.copy(trees = amount)
					"cars" -> info = info.copy(cars = amount)
					"perfumes" -> info = info.copy(perfumes = amount)
				}
			}
			return@map Aunt(number, info)
		}
	}

	override fun part1(input: List<Aunt>): Int {
		return input.first { it.info matches lookingForPart1 }.number
	}

	override fun part2(input: List<Aunt>): Int {
		return input.first { it.info matches lookingForPart2 }.number
	}
}