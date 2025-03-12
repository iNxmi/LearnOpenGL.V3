package com.nami.core

import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Transform(
    val position: Vector3f = Vector3f(),
    val rotation: Quaternionf = Quaternionf(),
    val scale: Vector3f = Vector3f(1f)
) {

    private val matrix = Matrix4f()
    fun matrix(): Matrix4f {
        update()
        return matrix
    }

    private val normalMatrix = Matrix3f()
    fun normalMatrix(): Matrix3f {
        update()
        return normalMatrix
    }

    private fun update() {
        matrix.identity()
            .translate(position)
            .rotate(rotation)
            .scale(scale)

        normalMatrix.set(Matrix4f(matrix).invert().transpose())
    }

}
