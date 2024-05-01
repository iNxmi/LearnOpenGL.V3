package com.nami.camera

import org.joml.Vector3f

class Plane(val position: Vector3f, val normal: Vector3f) {

    val distance = Vector3f(normal).dot(position)

    fun getSignedDistanceToPlane(position: Vector3f): Float {
        return Vector3f(normal).dot(position) - distance
    }

}