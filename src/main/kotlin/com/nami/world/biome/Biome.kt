package com.nami.world.biome

import com.nami.world.biome.biomes.*
import com.nami.world.block.Block
import com.nami.world.chunk.Chunk
import com.nami.world.feature.Feature
import de.articdive.jnoise.pipeline.JNoise
import org.joml.Vector3i

abstract class Biome(val id: String) {

    companion object {
        val set by lazy {
            setOf(
                BiomeBeach,
                BiomeBirchForest,
                BiomeDesert,
                BiomeError,
                BiomeJungleForest,
                BiomeMushroomForest,
                BiomeOakForest,
                BiomeSea,
                BiomeSpruceForest
            )
        }

        val map by lazy { set.associateBy { it.id } }

        fun evaluate(density: Float, moisture: Float, temperature: Float) = set.firstOrNull {
            it.density.contains(density) && it.moisture.contains(moisture) && it.temperature.contains(temperature)
        } ?: BiomeError

        fun get(id: String) = map[id]
    }

    abstract val density: ClosedFloatingPointRange<Float>
    abstract val moisture: ClosedFloatingPointRange<Float>
    abstract val temperature: ClosedFloatingPointRange<Float>

    open val features: Set<Pair<JNoise, Feature>> = setOf()

    open fun generate(
        position: Vector3i,
        density: Float,
        densityAbove: Float,
        moisture: Float,
        temperature: Float
    ): Block? = null

}