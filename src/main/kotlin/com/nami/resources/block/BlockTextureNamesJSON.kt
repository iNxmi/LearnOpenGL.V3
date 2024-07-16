package com.nami.resources.block

import kotlinx.serialization.Serializable

@Serializable
data class BlockTextureNamesJSON(
    val top: String,
    val bottom: String,

    val north: String,
    val east: String,
    val west: String,
    val south: String,
)