package com.nami.world

import com.nami.toFloatArray
import org.joml.Vector3f

data class Vertex(val position: Vector3f, val color: Vector3f, val normal: Vector3f) {

    companion object {
        const val SIZE_BYTES = 9 * Float.SIZE_BYTES
    }

    fun toFloatArray(): FloatArray {
        return position.toFloatArray() + color.toFloatArray() + normal.toFloatArray()
    }

}