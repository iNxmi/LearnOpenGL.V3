package com.nami.world.biome

import com.nami.world.biome.generator.BiomeGenerator

class BiomeTemplate(
    val name: String,
    val elevationRange: ClosedFloatingPointRange<Float>,
    val moistureRange: ClosedFloatingPointRange<Float>,
    val temperatureRange: ClosedFloatingPointRange<Float>,
    val generator: BiomeGenerator
) {
}