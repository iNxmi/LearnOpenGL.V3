package com.nami.resources.particle

import com.nami.json.JSONColor
import com.nami.json.JSONRange
import kotlinx.serialization.Serializable

@Serializable
class ParticleColorJSON(
    val color: JSONColor,
    val brightness: JSONRange<Float>
)