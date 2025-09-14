package com.nami

import com.nami.serializer.SerializerMatrix3f
import com.nami.serializer.SerializerMatrix4f
import com.nami.serializer.SerializerQuaternionf
import com.nami.serializer.SerializerVector3f
import kotlinx.serialization.Serializable
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

@Serializable
class Transform(
    @Serializable(with = SerializerVector3f::class)
    val position: Vector3f = Vector3f(),
    @Serializable(with = SerializerQuaternionf::class)
    val rotation: Quaternionf = Quaternionf(),
    @Serializable(with = SerializerVector3f::class)
    val scale: Vector3f = Vector3f(1f)
) {

    constructor(transform: Transform) : this(
        Vector3f(transform.position),
        Quaternionf(transform.rotation),
        Vector3f(transform.scale)
    )

    //Rotation in Deg

    @Serializable(with = SerializerMatrix4f::class)
    private val matrix = Matrix4f()
    fun matrix(): Matrix4f {
        update()
        return matrix
    }

    @Serializable(with = SerializerMatrix3f::class)
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
