package com.nami.resources.particle

import com.nami.resources.JSONRange
import kotlinx.serialization.Serializable

@Serializable
data class ParticleJSON(
    val timeInSeconds: JSONRange<Float>,
    val scale: JSONRange<Float> = JSONRange(1f, 1f),
    val colors: List<ParticleColorJSON>,
    val handler: String? = null
)