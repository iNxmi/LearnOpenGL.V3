package com.nami.world.collision

import org.joml.Vector3f

class CollisionPoints(
    val pointA: Vector3f,
    val pointB: Vector3f,
    val normal: Vector3f,
    val depth: Float,
    val isColliding: Boolean
)