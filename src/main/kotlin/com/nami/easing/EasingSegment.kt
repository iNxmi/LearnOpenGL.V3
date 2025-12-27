package com.nami.easing


data class EasingSegment(
    val range: ClosedFloatingPointRange<Float>,
    val equation: String
)