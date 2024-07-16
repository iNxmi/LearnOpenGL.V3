package com.nami

import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import java.util.*
import kotlin.math.roundToInt

fun Vector2f.toFloatArray(): FloatArray {
    return floatArrayOf(this.x, this.y)
}

fun Vector3f.toFloatArray(): FloatArray {
    return floatArrayOf(this.x, this.y, this.z)
}

fun Vector2i.toIntArray(): IntArray {
    return intArrayOf(this.x, this.y)
}

fun Vector3i.toIntArray(): IntArray {
    return intArrayOf(this.x, this.y, this.z)
}

fun String.snakeToUpperCamelCase(): String {
    val pattern = "_([a-z])".toRegex()
    return (replace(pattern) { it.groupValues[1].uppercase() }).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}

fun Int.Companion.random(range: IntRange): Int {
    return (Math.random().toFloat() * (range.endInclusive - range.start).toFloat() + range.start.toFloat()).roundToInt()
}

fun Float.Companion.random(range: ClosedFloatingPointRange<Float>): Float {
    return Math.random().toFloat() * (range.endInclusive - range.start) + range.start
}

fun IntRange.overlaps(range: IntRange): Boolean {
    val a1: Int = this.first
    val a2: Int = this.last

    val b1: Int = range.first
    val b2: Int = range.last

    val result = kotlin.math.max(a2, b2) - kotlin.math.min(a1, b1) < (a2 - a1) + (b2 - b1)
    return result
}