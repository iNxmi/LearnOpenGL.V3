package com.nami

import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i

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

