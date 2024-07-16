package com.nami.resources.particle

import kotlinx.serialization.Serializable

@Serializable
data class ParticleTimeJSON(
    val min: Float,
    val max: Float
)
