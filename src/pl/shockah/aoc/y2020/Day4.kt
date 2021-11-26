package pl.shockah.aoc.y2020

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pl.shockah.aoc.AdventTask
import pl.shockah.aoc.parseOrNull
import java.util.regex.Pattern

class Day4: AdventTask<List<Day4.Passport>, Int, Int>(2020, 4) {
	private val eyeColors = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
	private val hexRegex = Regex("#[\\da-f]{6}")
	private val cmHeightRegex = Pattern.compile("(\\d+)cm")
	private val inchHeightRegex = Pattern.compile("(\\d+)in")

	data class Passport(
		val fields: Map<String, String>
	)

	override fun parseInput(rawInput: String): List<Passport> {
		val entries = rawInput.split("\n\n")
		return entries.map {
			val rawFields = it.replace("\n", " ").split(" ")
			val fields = rawFields.map fieldMap@ {
				val split = it.split(":")
				return@fieldMap split[0] to split[1]
			}.toMap()
			return@map Passport(fields)
		}
	}

	override fun part1(input: List<Passport>): Int {
		return input.count { it.fields.keys.containsAll(listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")) }
	}

	override fun part2(input: List<Passport>): Int {
		return input.count { passport ->
			passport.fields["byr"]?.toIntOrNull()?.takeIf { it in 1920..2002 } ?: return@count false
			passport.fields["iyr"]?.toIntOrNull()?.takeIf { it in 2010..2020 } ?: return@count false
			passport.fields["eyr"]?.toIntOrNull()?.takeIf { it in 2020..2030 } ?: return@count false
			passport.fields["hgt"]?.takeIf { isHeightValid(it) } ?: return@count false
			passport.fields["hcl"]?.takeIf { it.matches(hexRegex) } ?: return@count false
			passport.fields["ecl"]?.takeIf { it in eyeColors } ?: return@count false
			passport.fields["pid"]?.takeIf { it.length == 9 }?.toIntOrNull() ?: return@count false
			return@count true
		}
	}

	private fun isHeightValid(height: String): Boolean {
		run {
			val cmHeight = cmHeightRegex.parseOrNull<Int>(height)
			if (cmHeight != null && cmHeight in 150..193)
				return true
		}

		run {
			val inchHeight = inchHeightRegex.parseOrNull<Int>(height)
			if (inchHeight != null && inchHeight in 59..76)
				return true
		}

		return false
	}

	class Tests {
		private val task = Day4()

		private val rawInput = """
			ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
			byr:1937 iyr:2017 cid:147 hgt:183cm
			
			iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
			hcl:#cfa07d byr:1929
			
			hcl:#ae17e1 iyr:2013
			eyr:2024
			ecl:brn pid:760753108 byr:1931
			hgt:179cm
			
			hcl:#cfa07d eyr:2025 pid:166559648
			iyr:2011 ecl:brn hgt:59in
			
			eyr:1972 cid:100
			hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926

			iyr:2019
			hcl:#602927 eyr:1967 hgt:170cm
			ecl:grn pid:012533040 byr:1946

			hcl:dab227 iyr:2012
			ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277

			hgt:59cm ecl:zzz
			eyr:2038 hcl:74454a iyr:2023
			pid:3556412378 byr:2007
			
			pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
			hcl:#623a2f

			eyr:2029 ecl:blu cid:129 byr:1989
			iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm

			hcl:#888785
			hgt:164cm byr:2001 iyr:2015 cid:88
			pid:545766238 ecl:hzl
			eyr:2022

			iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
		""".trimIndent()

		@Test
		fun part1() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(10, task.part1(input))
		}

		@Test
		fun part2() {
			val input = task.parseInput(rawInput)
			Assertions.assertEquals(6, task.part2(input))
		}
	}
}