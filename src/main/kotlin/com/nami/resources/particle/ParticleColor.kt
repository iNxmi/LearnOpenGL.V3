package com.nami.resources.particle

import com.nami.serializer.SerializerClosedFloatingPointRangeFloat
import com.nami.serializer.SerializerVector3fHexadecimal
import kotlinx.serialization.Serializable
import org.joml.Vector3f

@Serializable
class ParticleColor(
    @Serializable(with = SerializerVector3fHexadecimal::class)
    val color: Vector3f,
    @Serializable(with = SerializerClosedFloatingPointRangeFloat::class)
    val brightness: ClosedFloatingPointRange<Float>
)