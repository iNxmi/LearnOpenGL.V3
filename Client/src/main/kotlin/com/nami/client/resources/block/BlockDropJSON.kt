package com.nami.client.resources.block

import com.nami.client.json.JSONRange
import kotlinx.serialization.Serializable

@Serializable
data class BlockDropJSON(
    val item: String,
    val amount: JSONRange<Int> = JSONRange(1, 1),
    val rate: Float = 1.0f
)