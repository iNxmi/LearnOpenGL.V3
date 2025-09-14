package com.nami.serializer

import kotlinx.serialization.Serializable
import org.joml.Vector4f

@Serializable
data class JSONColor(
    val hex: String
) {

    fun toVector(): Vector4f {
        val color: Int = Integer.parseInt(hex, 16)

        val r = (color shr 16) and 0xFF
        val g = (color shr 8) and 0xFF
        val b = (color shr 0) and 0xFF
        val a = (color shr 24) and 0xFF

        return Vector4f(
            r.toFloat(),
            g.toFloat(),
            b.toFloat(),
            a.toFloat()
        ).div(255.0f)
    }

}