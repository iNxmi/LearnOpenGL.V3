package com.nami.graphics

import org.joml.Vector2f
import org.joml.Vector3f

data class Vertex(
    val position: Vector3f,
    val normal: Vector3f,
    val uv: Vector2f
)