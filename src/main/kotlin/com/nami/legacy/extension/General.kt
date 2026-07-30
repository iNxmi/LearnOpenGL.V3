package com.nami.extension

import java.util.Locale
import kotlin.math.roundToInt
import kotlin.random.Random

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