package com.nami.resources.item

import kotlinx.serialization.Serializable

@Serializable
data class ItemJSON(
    val tags: List<String>? = null,
    val weight: Float,
    val handler: String? = null
)