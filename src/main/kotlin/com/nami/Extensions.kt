package com.nami

import com.nami.json.*
import org.joml.*
import java.lang.Math
import java.util.*
import kotlin.math.roundToInt


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


fun Vector2f.json(): JSONVector2f {
    return JSONVector2f(this)
}

fun Vector2i.json(): JSONVector2i {
    return JSONVector2i(this)
}


fun Vector3f.json(): JSONVector3f {
    return JSONVector3f(this)
}

fun Vector3i.json(): JSONVector3i {
    return JSONVector3i(this)
}

fun Quaternionf.json(): JSONQuaternionf {
    return JSONQuaternionf(this)
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