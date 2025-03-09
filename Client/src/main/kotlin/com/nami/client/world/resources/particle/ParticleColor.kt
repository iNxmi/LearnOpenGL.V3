package com.nami.client.world.resources.particle

import com.nami.client.random
import org.joml.Vector3f

data class ParticleColor(
    val color: Vector3f,
    val brightness: ClosedFloatingPointRange<Float>
) {

    fun generate(): Vector3f {
        return Vector3f(color).mul(Float.random(brightness))
    }

}