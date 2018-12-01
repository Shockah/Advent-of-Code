package pl.shockah.godwit.swift

inline fun <T> T?.guard(block: () -> Nothing): T {
	return this ?: block()
}

inline fun <T1, T2> guard(
		t1: () -> T1?,
		t2: () -> T2?,
		block: () -> Nothing
): Tuple2<T1, T2> = Tuple2(
		t1() ?: block(),
		t2() ?: block()
)

inline fun <T1, T2, T3> guard(
		t1: () -> T1?,
		t2: () -> T2?,
		t3: () -> T3?,
		block: () -> Nothing
): Tuple3<T1, T2, T3> = Tuple3(
		t1() ?: block(),
		t2() ?: block(),
		t3() ?: block()
)

inline fun <T1, T2, T3, T4> guard(
		t1: () -> T1?,
		t2: () -> T2?,
		t3: () -> T3?,
		t4: () -> T4?,
		block: () -> Nothing
): Tuple4<T1, T2, T3, T4> = Tuple4(
		t1() ?: block(),
		t2() ?: block(),
		t3() ?: block(),
		t4() ?: block()
)