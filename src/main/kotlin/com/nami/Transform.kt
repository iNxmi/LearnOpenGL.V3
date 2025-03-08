package com.nami

import com.nami.json.JSONQuaternionf
import com.nami.json.JSONVector3f
import kotlinx.serialization.Serializable
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Transform(
    val position: Vector3f = Vector3f(),
    val rotation: Quaternionf = Quaternionf(),
    val scale: Vector3f = Vector3f(1f)
) {

    constructor(transform: Transform) : this(
        Vector3f(transform.position),
        Quaternionf(transform.rotation),
        Vector3f(transform.scale)
    )

    //Rotation in Deg

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

    @Serializable
    data class JSON(
        val position: JSONVector3f,
        val rotation: JSONQuaternionf,
        val scale: JSONVector3f
    ) {

        fun create(): Transform {
            return Transform(position.create(), rotation.create(), scale.create())
        }

    }

    fun json(): JSON {
        return JSON(position.json(), rotation.json(), scale.json())
    }

}
