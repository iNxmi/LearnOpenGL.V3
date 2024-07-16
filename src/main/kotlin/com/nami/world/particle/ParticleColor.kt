package com.nami.world.particle

import com.nami.random
import org.joml.Vector3f

data class ParticleColor(
    val color: Vector3f,
    val brightness: ClosedFloatingPointRange<Float>
) {

    fun generate(): Vector3f {
        return Vector3f(color).mul(Float.random(brightness))
    }

}