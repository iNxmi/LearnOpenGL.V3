package com.nami.resources.particle

import com.nami.resources.JSONRange
import kotlinx.serialization.Serializable

@Serializable
class ParticleColorJSON(
    val hex: String,
    val brightness: JSONRange<Float>
)