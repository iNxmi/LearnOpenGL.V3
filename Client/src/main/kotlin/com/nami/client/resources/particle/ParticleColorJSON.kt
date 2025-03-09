package com.nami.client.resources.particle

import com.nami.client.json.JSONColor
import com.nami.client.json.JSONRange
import kotlinx.serialization.Serializable

@Serializable
class ParticleColorJSON(
    val color: JSONColor,
    val brightness: JSONRange<Float>
)