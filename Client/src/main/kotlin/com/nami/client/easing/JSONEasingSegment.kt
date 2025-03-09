package com.nami.client.easing

import com.nami.client.json.JSONRange
import kotlinx.serialization.Serializable

@Serializable
data class JSONEasingSegment(
    val range: JSONRange<Float>,
    val equation: String
)