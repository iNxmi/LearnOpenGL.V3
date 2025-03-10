package com.nami.client.world.resources.biome

import com.nami.client.resources.Resources
import com.nami.client.resources.Resources.Companion.BLOCK
import com.nami.client.resources.Resources.Companion.FEATURE
import com.nami.client.resources.biome.BiomeFeatureJSON
import com.nami.client.resources.biome.BiomeRangeJSON
import com.nami.client.resources.biome.ResourceBiome
import com.nami.client.snakeToUpperCamelCase
import com.nami.client.world.World
import com.nami.client.world.resources.block.Block
import kotlinx.serialization.Serializable
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

        fun generate(): Block? {
            return handler.onGenerateBlock(world, position, factors)
        }

        @Serializable
        data class JSON(
            val id: String,
            val elevation: Float,
            val moisture: Float,
            val temperature: Float
        ) {

            fun create(world: World, position: Vector3i): Instance {
                val template = Resources.BIOME.get(id)
                val instance = template.create(world, position, Vector3f(elevation, moisture, temperature))
                return instance
            }

        }

        fun json(): JSON {
            return JSON(template.id, factors.x, factors.y, factors.z)
        }

    }

    @Serializable
    data class JSON(
        val elevation: BiomeRangeJSON,
        val moisture: BiomeRangeJSON,
        val temperature: BiomeRangeJSON,
        val features: List<com.nami.client.resources.biome.BiomeFeatureJSON>? = null
    ) {

        fun create(id: String): Biome {
            val handlerClass: Class<*> =
                try {
                    Class.forName("com.nami.client.world.resources.biome.handlers.BiomeHandler${id.snakeToUpperCamelCase()}")
                } catch (e: Exception) {
                    Class.forName("com.nami.client.world.resources.biome.handlers.DefaultBiomeHandler")
                }

            val elevationRange = IntRange(elevation.min, elevation.max)
            val moistureRange = IntRange(moisture.min, moisture.max)
            val temperatureRange = IntRange(temperature.min, temperature.max)

            val feat = mutableSetOf<BiomeFeature>()
            features?.forEach {
                val base = mutableSetOf<Block>()
                if (it.base != null)
                    it.base.forEach { blockID -> base.add(BLOCK.get(blockID)) }

                val feature = BiomeFeature(
                    FEATURE.get(it.feature),
                    base,
                    it.scale,
                    it.radius
                )
                feat.add(feature)
            }

            return Biome(
                id,
                handlerClass as Class<BiomeListener>,
                elevationRange,
                moistureRange,
                temperatureRange,
                feat
            )
        }

    }

}