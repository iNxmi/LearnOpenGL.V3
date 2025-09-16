package com.nami

import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.random.Random


fun Vector2f.toFloatArray(): FloatArray {
    return floatArrayOf(this.x, this.y)
}

fun Vector2i.toIntArray(): IntArray {
    return intArrayOf(this.x, this.y)
}


fun Vector3f.toFloatArray(): FloatArray {
    return floatArrayOf(this.x, this.y, this.z)
}

fun Vector3i.toIntArray(): IntArray {
    return intArrayOf(this.x, this.y, this.z)
}

fun String.snakeToUpperCamelCase(): String {
    val pattern = Regex("_([a-z])")
    return (replace(pattern) { it.groupValues[1].uppercase() }).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}

fun Random.next(range: IntRange): Int =
    this.next(range.start.toDouble()..range.endInclusive.toDouble()).roundToInt()

fun Random.next(range: ClosedFloatingPointRange<Float>): Float =
    this.next(range.start.toDouble()..range.endInclusive.toDouble()).toFloat()

fun Random.next(range: ClosedFloatingPointRange<Double>): Double =
    this.nextDouble() * (range.endInclusive - range.start) + range.start