package com.nami.world.chunk

import org.joml.Vector3i

data class Vertex(
    val position: Vector3i,
    val normal: Vector3i
) {

    fun toArray() = floatArrayOf(
        position.x.toFloat(),
        position.y.toFloat(),
        position.z.toFloat(),
        normal.x.toFloat(),
        normal.y.toFloat(),
        normal.z.toFloat()
    )

}