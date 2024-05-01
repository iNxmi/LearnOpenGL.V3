package com.nami.camera

import com.nami.constants.Directions
import com.nami.entity.Transform
import org.joml.Matrix4f
import org.joml.Vector3f

class Camera(var fov: Float, var aspect: Float, var near: Float, var far: Float) {
    val transform = Transform()

    val directionFront = Vector3f()
    val directionRight = Vector3f()

    private val projection = Matrix4f()
    fun projection(): Matrix4f {
        return projection.identity().perspective(fov, aspect, near, far)
    }

    private val view: Matrix4f = Matrix4f()
    fun view(): Matrix4f {
        directionRight.set(directionFront).cross(Directions.UP)
        return view.identity().lookAt(transform.position, Vector3f(transform.position).add(directionFront), Directions.UP)
    }

    fun frustum(): Frustum {
        return Frustum(this)
    }

}