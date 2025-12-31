package com.nami.world.biome

import com.nami.world.biome.biomes.*
import com.nami.world.block.Block
import com.nami.world.recipe.Recipe
import mu.KotlinLogging
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

        fun evaluate(elevation: Float, moisture: Float, temperature: Float) = set.firstOrNull {
            it.elevation.contains(elevation) && it.moisture.contains(moisture) && it.temperature.contains(temperature)
        } ?: BiomeError

        fun get(id: String) = map[id]

        fun create(
            position: Vector3i,
            elevation: Float,
            moisture: Float,
            temperature: Float
        ) = Instance(
            position = position,
            elevation = elevation,
            moisture = moisture,
            temperature = temperature,
            template = evaluate(elevation, moisture, temperature)
        )
    }

    abstract val elevation: ClosedFloatingPointRange<Float>
    abstract val moisture: ClosedFloatingPointRange<Float>
    abstract val temperature: ClosedFloatingPointRange<Float>

    open fun generate(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? = null

    data class Instance(
        val position: Vector3i,
        val elevation: Float,
        val moisture: Float,
        val temperature: Float,
        val template: Biome
    )

}