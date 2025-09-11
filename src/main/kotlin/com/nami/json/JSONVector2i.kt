package com.nami.json

import kotlinx.serialization.Serializable
import org.joml.Vector2i

@Serializable
class JSONVector2i(
    val x: Int,
    val y: Int
) {

    constructor(vector: Vector2i) : this(vector.x, vector.y)

    fun create() =  Vector2i(x, y)


}