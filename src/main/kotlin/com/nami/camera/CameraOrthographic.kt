package com.nami.camera

import org.joml.Matrix4f

class CameraOrthographic(
    var left: Float,
    var right: Float,
    var top: Float,
    var bottom: Float,
    var zNear: Float,
    var zFar: Float
) : Camera() {

    override fun projection(): Matrix4f {
        return projection.identity().ortho(left, right, bottom, top, zNear, zFar)
    }

    override fun view(): Matrix4f {
        return transform.matrix()
    }

}