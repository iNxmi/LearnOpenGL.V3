package com.nami.legacy.camera

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class OrthographicCamera(
    var left: Float,
    var right: Float,

    var top: Float,
    var bottom: Float,

    var zNear: Float,
    var zFar: Float,

    override val position: Vector3f = Vector3f(),
    override val rotation: Quaternionf = Quaternionf()
) : Camera {

    override val projection = Matrix4f()
        get() = field.identity().ortho(left, right, bottom, top, zNear, zFar)

    override val view = Matrix4f()
        get() = field.identity().translate(position).rotate(rotation)

}