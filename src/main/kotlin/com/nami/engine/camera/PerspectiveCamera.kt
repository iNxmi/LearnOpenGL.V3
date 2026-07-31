package com.nami.engine.camera

import com.nami.Directions
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class PerspectiveCamera(
    var fov: Float,

    var aspect: Float,

    var near: Float,
    var far: Float,

    override val position: Vector3f = Vector3f(),
    override val rotation: Quaternionf = Quaternionf()
) : Camera {

    override val projection = Matrix4f()
        get() = field.identity().perspective(fov, aspect, near, far)

    override val view = Matrix4f()
        get() {
            val direction = Vector3f(0f, 0f, -1f).rotate(rotation)
            return field.identity().lookAt(position, Vector3f(position).add(direction), Directions.UP.vector)
        }

}