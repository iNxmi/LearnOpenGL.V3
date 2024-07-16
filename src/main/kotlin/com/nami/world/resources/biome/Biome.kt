package com.nami.world.resources.biome

import com.nami.resources.biome.ResourceBiome
import com.nami.world.World
import org.joml.Vector3f
import org.joml.Vector3i

class Biome(
    id: String,
    private val handlerClass: Class<BiomeListener>,

    val elevationRange: IntRange,
    val moistureRange: IntRange,
    val temperatureRange: IntRange,
    val features: Set<BiomeFeature>
) : ResourceBiome(id) {

    fun create(world: World, position: Vector3i, factors: Vector3f): Instance {
        val handler = handlerClass.getDeclaredConstructor().newInstance()
        return Instance(this, handler, world, position, factors)
    }

    class Instance(
        val template: Biome,
        val handler: BiomeListener,

        val world: World,
        val position: Vector3i,
        val factors: Vector3f
    ) {

        fun generate(): String? {
            return handler.onGenerateBlock(world, position, factors)
        }

    }

}