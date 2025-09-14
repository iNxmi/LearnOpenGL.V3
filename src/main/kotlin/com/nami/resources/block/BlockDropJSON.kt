package com.nami.resources.block

import com.nami.serializer.SerializerIntRange
import kotlinx.serialization.Serializable

@Serializable
data class BlockDropJSON(
    val item: String,
    @Serializable(with = SerializerIntRange::class)
    val amount: IntRange = 1..1,
    val probability: Float = 1.0f
)