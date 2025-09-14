package com.nami.resources.particle

import com.nami.serializer.JSONColor
import com.nami.serializer.JSONRange
import kotlinx.serialization.Serializable

@Serializable
class ParticleColorJSON(
    val color: JSONColor,
    val brightness: JSONRange<Float>
)