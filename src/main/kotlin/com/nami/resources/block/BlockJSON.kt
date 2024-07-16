package com.nami.resources.block

import kotlinx.serialization.Serializable

@Serializable
data class BlockJSON(
    val textures: BlockTextureNamesJSON,
    val layer: String,
    val resistance: Map<String, Float> = mapOf(),
    val drops: List<BlockDropJSON>? = null,
    val tags: List<String>? = null
)
