package com.nami.entity

import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f

class Transform(
    var position: Vector3f = Vector3f(),
    var rotation: Vector3f = Vector3f(),
    var scale: Vector3f = Vector3f(1f)
) {

    //Rotation in Deg

    private val positionLast = Vector3f(Float.MAX_VALUE)
    private val rotationLast = Vector3f(Float.MAX_VALUE)
    private val scaleLast = Vector3f(Float.MAX_VALUE)

    private val modelMatrix = Matrix4f()
    fun modelMatrix(): Matrix4f {
        update()
        return modelMatrix
    }

    private val normalMatrix = Matrix3f()
    fun normalMatrix(): Matrix3f {
        update()
        return normalMatrix
    }

    private fun update() {
        if (position != positionLast || rotation != rotationLast || scale != scaleLast) {
            modelMatrix.identity()
//                .scale(scale)
//                .rotateX(Math.toRadians(rotation.x.toDouble()).toFloat())
//                .rotateY(Math.toRadians(rotation.y.toDouble()).toFloat())
//                .rotateY(Math.toRadians(rotation.z.toDouble()).toFloat())
//                .translate(position)
                .translate(position)
                .rotateX(Math.toRadians(rotation.x.toDouble()).toFloat())
                .rotateY(Math.toRadians(rotation.y.toDouble()).toFloat())
                .rotateY(Math.toRadians(rotation.z.toDouble()).toFloat())
                .scale(scale)

            normalMatrix.set(Matrix4f(modelMatrix).invert().transpose())

            positionLast.set(position)
            rotationLast.set(rotation)
            scaleLast.set(scale)
        }
    }

}
