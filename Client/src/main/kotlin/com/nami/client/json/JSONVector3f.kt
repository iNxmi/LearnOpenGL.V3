package com.nami.client.json

import kotlinx.serialization.Serializable
import org.joml.Vector3f

@Serializable
class JSONVector3f(
    val x: Float,
    val y: Float,
    val z: Float
) {

    constructor(vector: Vector3f) : this(vector.x, vector.y, vector.z)

    fun create(): Vector3f {
        return Vector3f(x, y, z)
    }

}