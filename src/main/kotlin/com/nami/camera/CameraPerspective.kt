package com.nami.camera

import com.nami.Directions
import org.joml.Matrix4f
import org.joml.Vector3f

class CameraPerspective(
    var fov: Float,
    var aspect: Float,
    var near: Float,
    var far: Float
) : Camera() {

    val directionFront = Vector3f()
    val directionRight = Vector3f()

    override fun projection(): Matrix4f {
        return projection.identity().perspective(fov, aspect, near, far)
    }

    override fun view(): Matrix4f {
        directionRight.set(directionFront).cross(Directions.UP)
        return view.identity()
            .lookAt(transform.position, Vector3f(transform.position).add(directionFront), Directions.UP)
    }

}