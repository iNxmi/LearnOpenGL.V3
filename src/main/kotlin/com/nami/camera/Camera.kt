package com.nami.camera

import com.nami.constants.Directions
import org.joml.Matrix4f
import org.joml.Vector3f

class Camera(fov: Float, aspect: Float, zNear: Float, zFar: Float) {
    var fov = fov
        set(value) {
            field = value
            updateProjection()
        }

    var aspect = aspect
        set(value) {
            field = value
            updateProjection()
        }

    var zNear = zNear
        set(value) {
            field = value
            updateProjection()
        }

    var zFar = zFar
        set(value) {
            field = value
            updateProjection()
        }

    val position = Vector3f()
    val direction = Vector3f()

    val projection: Matrix4f = Matrix4f()
    val view: Matrix4f = Matrix4f()
        get() = field.identity().lookAt(position, Vector3f(position).add(direction), Directions.UP)

    init {
        updateProjection()
    }

    private fun updateProjection() {
        projection.identity().perspective(this.fov, this.aspect, this.zNear, this.zFar)
    }

}