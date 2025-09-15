package com.nami.resources.block

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class BlockDropJSON(
    val item: String,
    @Contextual val amount: IntRange = 1..1,
    val probability: Float = 1.0f
)