package com.nami.resources.biome

import kotlinx.serialization.Serializable

@Serializable
class BiomeFeatureJSON(
    val feature: String,
    val base: Array<String>? = null,
    val scale: Float,
    val radius: Int
)
