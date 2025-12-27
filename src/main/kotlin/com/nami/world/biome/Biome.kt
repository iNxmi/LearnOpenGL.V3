package com.nami.world.biome

import com.nami.world.block.Block
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.joml.Vector3i

abstract class Biome(val id: String) {

    companion object {
        val set = setOf(
            BiomeBeach,
            BiomeBirchForest,
            BiomeDesert,
            BiomeJungleForest,
            BiomeMushroomForest,
            BiomeOakForest,
            BiomeSea,
            BiomeSpruceForest,
        )

        val map = set.associateBy { it.id }

        fun evaluate(elevation: Float, moisture: Float, temperature: Float) = set.firstOrNull {
            it.elevation.contains(elevation)
                    && it.moisture.contains(moisture)
                    && it.temperature.contains(temperature)
        } ?: BiomeInvalid

        fun get(id: String) = map[id]

        fun create(
            position: Vector3i,
            elevation: Float,
            moisture: Float,
            temperature: Float
        ) = Instance(
            position,
            elevation,
            moisture,
            temperature,
            evaluate(elevation, moisture, temperature)
        )
    }

    abstract val elevation: ClosedFloatingPointRange<Float>
    abstract val moisture: ClosedFloatingPointRange<Float>
    abstract val temperature: ClosedFloatingPointRange<Float>

    open fun generate(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? = null

    @Serializable
    data class Instance(
        @Contextual val position: Vector3i,
        val elevation: Float,
        val moisture: Float,
        val temperature: Float,
        @Contextual val template: Biome
    )

}