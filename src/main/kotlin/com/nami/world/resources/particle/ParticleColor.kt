package com.nami.world.resources.particle

import com.nami.random
import com.nami.serializer.SerializerVector3f
import kotlinx.serialization.Serializable
import org.joml.Vector3f

@Serializable
data class ParticleColor(
    @Serializable(with = SerializerVector3f::class)
    val color: Vector3f,
    val brightness: ClosedFloatingPointRange<Float>
) {
    fun generate() = Vector3f(color).mul(Float.random(brightness))
}