package com.nami.world.resources.particle

import com.nami.next
import com.nami.serializer.SerializerVector3f
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.joml.Vector3f
import kotlin.random.Random

@Serializable
data class ParticleColor(
    @Serializable(with = SerializerVector3f::class)
    val color: Vector3f,
    val brightness: ClosedFloatingPointRange<Float>
) {
    @Transient
    val random = Random(System.currentTimeMillis())
    fun generate() = Vector3f(color).mul(random.next(brightness))
}