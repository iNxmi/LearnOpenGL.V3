package com.nami.world.biome

import com.nami.world.block.Block
import org.joml.Vector3f
import org.joml.Vector3i

class Biome(
    val id: String,
    val elevationRange: IntRange,
    val moistureRange: IntRange,
    val temperatureRange: IntRange,
    val handler: BiomeListener?
) {
    fun create(position: Vector3i, factors: Vector3f): Instance {
        return Instance(this, position, factors)
    }

    class Instance(val template: Biome, val position: Vector3i, val factors: Vector3f) {
        fun generate(): String? {
            val handler = template.handler ?: return "invalid"
            return handler.onGenerateBlock(position, factors)
        }

    }

}