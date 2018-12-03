package pl.shockah.aoc

import pl.shockah.aoc.swift.*
import java.util.regex.Pattern

fun <A> Pattern.parse(input: String, a: (String?) -> A): A {
	val matcher = matcher(input)
	if (!matcher.find())
		throw IllegalArgumentException()
	return a(matcher.group(1))
}

fun <A, B> Pattern.parse(
		input: String,
		a: (String?) -> A,
		b: (String?) -> B
): Tuple2<A, B> {
	val matcher = matcher(input)
	if (!matcher.find())
		throw IllegalArgumentException()
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
	if (!matcher.find())
		throw IllegalArgumentException()
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
	if (!matcher.find())
		throw IllegalArgumentException()
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
	if (!matcher.find())
		throw IllegalArgumentException()
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
	if (!matcher.find())
		throw IllegalArgumentException()
	return Tuple6(
			a(matcher.group(1)),
			b(matcher.group(2)),
			c(matcher.group(3)),
			d(matcher.group(4)),
			e(matcher.group(5)),
			f(matcher.group(6))
	)
}

fun <Input : Any, Output : Any> ((Input) -> Output).orThrow(): (Input?) -> Output {
	return { arg -> if (arg == null) throw NullPointerException() else this(arg) }
}

val IntPatternParser: (String?) -> Int = { s: String -> s.toInt() }.orThrow()