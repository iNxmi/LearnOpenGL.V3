package com.nami.world.biome

import com.nami.world.resources.block.Block
import org.joml.Vector3i

abstract class Biome {

    companion object {

        val register = setOf(
            BiomeBeach,
            BiomeBirchForest,
            BiomeDesert,
            BiomeJungleForest,
            BiomeMushroomForest,
            BiomeOakForest,
            BiomeSea,
            BiomeSpruceForest,
        )

        fun evaluate(elevation: Float, moisture: Float, temperature: Float) = register.firstOrNull {
            it.elevation.contains(elevation)
                    && it.moisture.contains(moisture)
                    && it.temperature.contains(temperature)
        } ?: BiomeInvalid

    }

    abstract val elevation: ClosedFloatingPointRange<Float>
    abstract val moisture: ClosedFloatingPointRange<Float>
    abstract val temperature: ClosedFloatingPointRange<Float>

    open fun generateBlock(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? = null

}