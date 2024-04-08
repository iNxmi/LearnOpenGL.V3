package com.nami.mesh

import org.joml.Vector2f
import org.joml.Vector3f

data class Vertex(val position: Vector3f, val normal: Vector3f, val uv: Vector2f) {

    companion object {
        @JvmStatic
        val SIZE_BYTES = (3 + 3 + 2) * Float.SIZE_BYTES
    }

    val data = floatArrayOf(
        position.x, position.y, position.z,
        normal.x, normal.y, normal.z,
        uv.x, uv.y
    )

}