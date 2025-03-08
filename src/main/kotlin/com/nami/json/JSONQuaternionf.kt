package com.nami.json

import kotlinx.serialization.Serializable
import org.joml.Quaternionf

@Serializable
data class JSONQuaternionf(
    val x: Float,
    val y: Float,
    val z: Float,
    val w: Float
) {

    constructor(quaternion: Quaternionf) : this(quaternion.x, quaternion.y, quaternion.z, quaternion.w)

    fun create(): Quaternionf {
        return Quaternionf(x, y, z, w)
    }

}
