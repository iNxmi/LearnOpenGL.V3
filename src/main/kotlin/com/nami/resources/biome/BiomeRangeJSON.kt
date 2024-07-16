package com.nami.resources.biome

import kotlinx.serialization.Serializable

@Serializable
data class BiomeRangeJSON(
    val min: Int,
    val max: Int
)