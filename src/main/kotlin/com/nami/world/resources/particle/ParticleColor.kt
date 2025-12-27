package com.nami.world.resources.particle

import com.nami.next
import org.joml.Vector3f
import kotlin.random.Random


data class ParticleColor(

    val color: Vector3f,
    val brightness: ClosedFloatingPointRange<Float>
) {
    @Transient
    val random = Random(System.currentTimeMillis())
    fun generate() = Vector3f(color).mul(random.next(brightness))
}