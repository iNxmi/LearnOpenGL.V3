package com.nami.json

import kotlinx.serialization.Serializable
import org.joml.Vector3i

@Serializable
class JSONVector3i(
    val x: Int,
    val y: Int,
    val z: Int
) {

    constructor(vector: Vector3i) : this(vector.x, vector.y, vector.z)

    fun create(): Vector3i {
        return Vector3i(x, y, z)
    }

}