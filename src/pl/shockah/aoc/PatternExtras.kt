package pl.shockah.aoc

import pl.shockah.aoc.swift.*
import java.math.BigInteger
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
internal inline fun <reified T> parseGroup(group: String): T {
	return when (T::class) {
		String::class -> group as T
		Int::class -> group.toInt() as T
		Long::class -> group.toLong() as T
		BigInteger::class -> group.toBigInteger() as T
		Char::class -> group[0] as T
		else -> throw IllegalArgumentException()
	}
}

@PublishedApi
internal inline fun <reified T> parseGroupOrNull(group: String): T? {
	return when (T::class) {
		String::class -> group as T?
		Int::class -> group.toIntOrNull() as T?
		Long::class -> group.toLongOrNull() as T?
		BigInteger::class -> group.toBigIntegerOrNull() as T?
		Char::class -> group[0] as T?
		else -> null
	}
}

inline fun <reified A> Pattern.parse(input: String): A {
	val matcher = matcher(input)
	require(matcher.find())
	return parseGroup(matcher.group(1))
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

inline fun <reified A> Pattern.parseOrNull(input: String): A? {
	val matcher = matcher(input)
	if (!matcher.find())
		return null
	return parseGroupOrNull(matcher.group(1))
}

inline fun <reified A, reified B> Pattern.parse2OrNull(input: String): Tuple2<A, B>? {
	val matcher = matcher(input)
	if (!matcher.find())
		return null
	return Tuple2(
			parseGroupOrNull<A>(matcher.group(1)),
			parseGroupOrNull<B>(matcher.group(2))
	).takeIf { it.first != null && it.second != null }?.let {
		Tuple2<A, B>(
				it.first!!,
				it.second!!
		)
	}
}

inline fun <reified A, reified B, reified C> Pattern.parse3OrNull(input: String): Tuple3<A, B, C>? {
	val matcher = matcher(input)
	if (!matcher.find())
		return null
	return Tuple3(
			parseGroupOrNull<A>(matcher.group(1)),
			parseGroupOrNull<B>(matcher.group(2)),
			parseGroupOrNull<C>(matcher.group(3))
	).takeIf { it.first != null && it.second != null && it.third != null }?.let {
		Tuple3(
				it.first!!,
				it.second!!,
				it.third!!
		)
	}
}

inline fun <reified A, reified B, reified C, reified D> Pattern.parse4OrNull(input: String): Tuple4<A, B, C, D>? {
	val matcher = matcher(input)
	if (!matcher.find())
		return null
	return Tuple4(
			parseGroupOrNull<A>(matcher.group(1)),
			parseGroupOrNull<B>(matcher.group(2)),
			parseGroupOrNull<C>(matcher.group(3)),
			parseGroupOrNull<D>(matcher.group(4))
	).takeIf { it.first != null && it.second != null && it.third != null && it.fourth != null }?.let {
		Tuple4(
				it.first!!,
				it.second!!,
				it.third!!,
				it.fourth!!
		)
	}
}

inline fun <reified A, reified B, reified C, reified D, reified E> Pattern.parse5OrNull(input: String): Tuple5<A, B, C, D, E>? {
	val matcher = matcher(input)
	if (!matcher.find())
		return null
	return Tuple5(
			parseGroupOrNull<A>(matcher.group(1)),
			parseGroupOrNull<B>(matcher.group(2)),
			parseGroupOrNull<C>(matcher.group(3)),
			parseGroupOrNull<D>(matcher.group(4)),
			parseGroupOrNull<E>(matcher.group(5))
	).takeIf { it.first != null && it.second != null && it.third != null && it.fourth != null && it.fifth != null }?.let {
		Tuple5(
				it.first!!,
				it.second!!,
				it.third!!,
				it.fourth!!,
				it.fifth!!
		)
	}
}

inline fun <reified A, reified B, reified C, reified D, reified E, reified F> Pattern.parse6OrNull(input: String): Tuple6<A, B, C, D, E, F>? {
	val matcher = matcher(input)
	if (!matcher.find())
		return null
	return Tuple6(
			parseGroupOrNull<A>(matcher.group(1)),
			parseGroupOrNull<B>(matcher.group(2)),
			parseGroupOrNull<C>(matcher.group(3)),
			parseGroupOrNull<D>(matcher.group(4)),
			parseGroupOrNull<E>(matcher.group(5)),
			parseGroupOrNull<F>(matcher.group(6))
	).takeIf { it.first != null && it.second != null && it.third != null && it.fourth != null && it.fifth != null && it.sixth != null }?.let {
		Tuple6(
				it.first!!,
				it.second!!,
				it.third!!,
				it.fourth!!,
				it.fifth!!,
				it.sixth!!
		)
	}
}

fun <Input: Any, Output: Any> ((Input) -> Output).orThrow(): (Input?) -> Output {
	return { arg -> if (arg == null) throw NullPointerException() else this(arg) }
}

val StringPatternParser: (String?) -> String = { s: String -> s }.orThrow()
val IntPatternParser: (String?) -> Int = { s: String -> s.toInt() }.orThrow()