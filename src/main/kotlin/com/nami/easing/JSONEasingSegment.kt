package com.nami.easing

import com.nami.json.JSONRange
import kotlinx.serialization.Serializable

@Serializable
data class JSONEasingSegment(
    val range: JSONRange<Float>,
    val equation: String
)