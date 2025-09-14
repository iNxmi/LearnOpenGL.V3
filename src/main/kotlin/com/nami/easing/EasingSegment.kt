package com.nami.easing

import kotlinx.serialization.Serializable

@Serializable
data class EasingSegment(
    val range: ClosedFloatingPointRange<Float>,
    val equation: String
)