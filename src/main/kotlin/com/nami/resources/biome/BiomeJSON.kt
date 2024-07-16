package com.nami.resources.biome

import kotlinx.serialization.Serializable

@Serializable
data class BiomeJSON(
    val elevation: BiomeRangeJSON,
    val moisture: BiomeRangeJSON,
    val temperature: BiomeRangeJSON,
    val features: List<BiomeFeatureJSON>? = null
)