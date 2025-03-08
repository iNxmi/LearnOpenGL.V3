package com.nami.json

import kotlinx.serialization.Serializable
import org.joml.Vector2f

@Serializable
class JSONVector2f(
    val x: Float,
    val y: Float
) {

    constructor(vector: Vector2f) : this(vector.x, vector.y)

    fun create(): Vector2f {
        return Vector2f(x, y)
    }

}