package com.nami.camera

import com.nami.Transform
import org.joml.Matrix4f

abstract class Camera(
    val transform: Transform = Transform()
) {
    protected val projection = Matrix4f()
    abstract fun projection(): Matrix4f

    protected val view = Matrix4f()
    abstract fun view(): Matrix4f
}