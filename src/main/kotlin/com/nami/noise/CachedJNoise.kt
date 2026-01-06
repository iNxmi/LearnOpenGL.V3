package com.nami.noise

import de.articdive.jnoise.pipeline.JNoise
import org.joml.Vector4f
import kotlin.math.round
import kotlin.math.roundToInt

class CachedJNoise(
    val jNoise: JNoise,
    val scale: Float = 1.0f
) {

    val cache = mutableMapOf<Long, Float>()
    fun evaluate(x: Float, y: Float, z: Float): Float {
        val xQuantized = getQuantized(x, scale)
        val yQuantized = getQuantized(y, scale)
        val zQuantized = getQuantized(z, scale)

        val key = getKey(x, y, z)

        if (!cache.containsKey())
    }

    fun getQuantized(value: Float, scale: Float): Int = (value * scale).roundToInt()

    fun getKey(x: Float, y: Float, z: Float): Long {
        val xQuantized = getQuantized(x, scale)
        val yQuantized = getQuantized(y, scale)
        val zQuantized = getQuantized(z, scale)

        return
    }

    fun getKey(x: Float, y: Float): Long {
        val xQuantized = getQuantized(x, scale)
        val yQuantized = getQuantized(y, scale)
    }

}

fun JNoise.toCached(): CachedJNoise {
    return CachedJNoise(this)
}