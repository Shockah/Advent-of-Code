package pl.shockah.aoc

import pl.shockah.aoc.swift.*
import java.util.regex.Pattern

fun <A> Pattern.parse(input: String, a: (String?) -> A): A {
	val matcher = matcher(input)
	require(matcher.find())
	return a(matcher.group(1))
}

fun <A, B> Pattern.parse(
		input: String,
		a: (String?) -> A,
		b: (String?) -> B
): Tuple2<A, B> {
	val matcher = matcher(input)
	require(matcher.find())
	return Tuple2(
			a(matcher.group(1)),
			b(matcher.group(2))
	)
}

fun <A, B, C> Pattern.parse(
		input: String,
		a: (String?) -> A,
		b: (String?) -> B,
		c: (String?) -> C
): Tuple3<A, B, C> {
	val matcher = matcher(input)
	require(matcher.find())
	return Tuple3(
			a(matcher.group(1)),
			b(matcher.group(2)),
			c(matcher.group(3))
	)
}

fun <A, B, C, D> Pattern.parse(
		input: String,
		a: (String?) -> A,
		b: (String?) -> B,
		c: (String?) -> C,
		d: (String?) -> D
): Tuple4<A, B, C, D> {
	val matcher = matcher(input)
	require(matcher.find())
	return Tuple4(
			a(matcher.group(1)),
			b(matcher.group(2)),
			c(matcher.group(3)),
			d(matcher.group(4))
	)
}

fun <A, B, C, D, E> Pattern.parse(
		input: String,
		a: (String?) -> A,
		b: (String?) -> B,
		c: (String?) -> C,
		d: (String?) -> D,
		e: (String?) -> E
): Tuple5<A, B, C, D, E> {
	val matcher = matcher(input)
	require(matcher.find())
	return Tuple5(
			a(matcher.group(1)),
			b(matcher.group(2)),
			c(matcher.group(3)),
			d(matcher.group(4)),
			e(matcher.group(5))
	)
}

fun <A, B, C, D, E, F> Pattern.parse(
		input: String,
		a: (String?) -> A,
		b: (String?) -> B,
		c: (String?) -> C,
		d: (String?) -> D,
		e: (String?) -> E,
		f: (String?) -> F
): Tuple6<A, B, C, D, E, F> {
	val matcher = matcher(input)
	require(matcher.find())
	return Tuple6(
			a(matcher.group(1)),
			b(matcher.group(2)),
			c(matcher.group(3)),
			d(matcher.group(4)),
			e(matcher.group(5)),
			f(matcher.group(6))
	)
}

@PublishedApi
internal inline fun <reified T> parseGroup(group: String?): T {
	return when (T::class) {
		String::class -> group as T
		Int::class -> group!!.toInt() as T
		Char::class -> group!![0] as T
		else -> throw IllegalArgumentException()
	}
}

inline fun <reified A, reified B> Pattern.parse2(input: String): Tuple2<A, B> {
	val matcher = matcher(input)
	require(matcher.find())
	return Tuple2(
			parseGroup(matcher.group(1)),
			parseGroup(matcher.group(2))
	)
}

inline fun <reified A, reified B, reified C> Pattern.parse3(input: String): Tuple3<A, B, C> {
	val matcher = matcher(input)
	require(matcher.find())
	return Tuple3(
			parseGroup(matcher.group(1)),
			parseGroup(matcher.group(2)),
			parseGroup(matcher.group(3))
	)
}

inline fun <reified A, reified B, reified C, reified D> Pattern.parse4(input: String): Tuple4<A, B, C, D> {
	val matcher = matcher(input)
	require(matcher.find())
	return Tuple4(
			parseGroup(matcher.group(1)),
			parseGroup(matcher.group(2)),
			parseGroup(matcher.group(3)),
			parseGroup(matcher.group(4))
	)
}

inline fun <reified A, reified B, reified C, reified D, reified E> Pattern.parse5(input: String): Tuple5<A, B, C, D, E> {
	val matcher = matcher(input)
	require(matcher.find())
	return Tuple5(
			parseGroup(matcher.group(1)),
			parseGroup(matcher.group(2)),
			parseGroup(matcher.group(3)),
			parseGroup(matcher.group(4)),
			parseGroup(matcher.group(5))
	)
}



inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Pattern.parse6(input: String): Tuple6<A, B, C, D, E, F> {
	val matcher = matcher(input)
	require(matcher.find())
	return Tuple6(
			parseGroup(matcher.group(1)),
			parseGroup(matcher.group(2)),
			parseGroup(matcher.group(3)),
			parseGroup(matcher.group(4)),
			parseGroup(matcher.group(5)),
			parseGroup(matcher.group(6))
	)
}

fun <Input: Any, Output: Any> ((Input) -> Output).orThrow(): (Input?) -> Output {
	return { arg -> if (arg == null) throw NullPointerException() else this(arg) }
}

val StringPatternParser: (String?) -> String = { s: String -> s }.orThrow()
val IntPatternParser: (String?) -> Int = { s: String -> s.toInt() }.orThrow()