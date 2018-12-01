package pl.shockah.godwit.geom

import pl.shockah.godwit.geom.polygon.Polygonable
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.superclasses

interface Shape {
	companion object {
		private val collisionHandlers: MutableMap<Pair<KClass<out Shape>, KClass<out Shape>>, (Shape, Shape) -> Boolean> = mutableMapOf()

		@Suppress("UNCHECKED_CAST")
		fun <A : Shape, B : Shape> registerCollisionHandler(first: KClass<A>, second: KClass<B>, handler: (A, B) -> Boolean) {
			collisionHandlers[Pair(first, second)] = handler as ((Shape, Shape) -> Boolean)
		}

		fun <A : Shape, B : Shape> shapesCollide(first: A, second: B): Boolean {
			val handler = findHandler(first::class, second::class)
			if (handler != null)
				return handler(first, second)

			if (first is Polygonable.Closed && second is Polygonable.Closed)
				return first.asClosedPolygon() collides second.asClosedPolygon()

			if (first is Polygonable.Open && second is Polygonable.Open)
				return first.asPolygon() collides second.asPolygon()

			throw UnsupportedOperationException("${first::class.simpleName} --><-- ${second::class.simpleName} collision isn't implemented.")
		}

		private fun findHandler(first: KClass<out Shape>, second: KClass<out Shape>): ((Shape, Shape) -> Boolean)? {
			return collisionHandlers[Pair(first, second)] ?: collisionHandlers[Pair(second, first)] ?: findHandlerFromSupertypes(first, second)
		}

		@Suppress("UNCHECKED_CAST")
		private fun findHandlerFromSupertypes(first: KClass<out Shape>, second: KClass<out Shape>): ((Shape, Shape) -> Boolean)? {
			for (firstSubclass in first.superclasses.filter { it != Shape::class && it.isSubclassOf(Shape::class) }.map { it as KClass<out Shape> }) {
				val result = findHandler(firstSubclass, second)
				if (result != null)
					return result
			}
			for (secondSubclass in second.superclasses.filter { it != Shape::class && it.isSubclassOf(Shape::class) }.map { it as KClass<out Shape> }) {
				val result = findHandler(first, secondSubclass)
				if (result != null)
					return result
			}
			return null
		}
	}

	val boundingBox: Rectangle

	val center: Vec2
		get() = boundingBox.center

	fun copy(): Shape

	fun translate(vector: Vec2)
	fun mirror(horizontal: Boolean, vertical: Boolean)
	fun scale(scale: Float)

	infix fun collides(other: Shape): Boolean {
		return Shape.shapesCollide(this, other)
	}

	interface Filled : Shape {
		operator fun contains(point: Vec2): Boolean
	}

	interface Outline : Shape {
	}
}