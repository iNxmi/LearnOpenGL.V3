package com.nami.resources.particle

import com.nami.random
import com.nami.serializer.SerializerVector3fHexadecimal
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.joml.Vector3f

@Serializable
class ParticleColor(
    @Serializable(with = SerializerVector3fHexadecimal::class)
    val color: Vector3f,
    @Contextual val brightness: ClosedFloatingPointRange<Float>
) {
    fun generate() = Vector3f(color).mul(Float.random(brightness))
}