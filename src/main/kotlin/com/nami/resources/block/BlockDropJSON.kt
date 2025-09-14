package com.nami.resources.block

import com.nami.json.JSONRange
import kotlinx.serialization.Serializable

@Serializable
data class BlockDropJSON(
    val item: String,
    val amount: JSONRange<Int> = JSONRange(1, 1),
    val probability: Float = 1.0f
)