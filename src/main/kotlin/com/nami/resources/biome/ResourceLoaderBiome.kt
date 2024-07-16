package com.nami.resources.biome

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.snakeToUpperCamelCase
import com.nami.world.resources.biome.Biome
import com.nami.world.resources.biome.BiomeFeature
import com.nami.world.resources.biome.handlers.DefaultBiomeHandler
import com.nami.world.resources.block.Block
import kotlinx.serialization.json.Json
import org.joml.Vector3f
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.roundToInt

class ResourceLoaderBiome : Resources<Biome>(GamePath.biome, "biome", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Biome {
        val jsonString = Files.readString(path)
        val json: BiomeJSON = Json.decodeFromString<BiomeJSON>(jsonString)

        val elevationRange = IntRange(json.elevation.min, json.elevation.max)
        val moistureRange = IntRange(json.moisture.min, json.moisture.max)
        val temperatureRange = IntRange(json.temperature.min, json.temperature.max)

        var features = mutableSetOf<BiomeFeature>()
        json.features?.forEach {
            var base = mutableSetOf<Block>()
            if (it.base != null)
                it.base.forEach { blockID -> base.add(BLOCK.get(blockID)) }

            val feature = BiomeFeature(
                FEATURE.get(it.feature),
                base,
                it.scale,
                it.radius
            )
            features.add(feature)
        }

        val handlerClass: Class<*> =
            try {
                Class.forName("com.nami.world.resources.biome.handlers.BiomeHandler${id.snakeToUpperCamelCase()}")
            } catch (e: Exception) {
                Class.forName("com.nami.world.resources.biome.handlers.DefaultBiomeHandler")
            }

        return Biome(
            id,
            handlerClass as Class<com.nami.world.resources.biome.BiomeListener>,

            elevationRange,
            moistureRange,
            temperatureRange,
            features
        )
    }

    override fun onLoadCompleted() {

    }

    val nullBiome = Biome("null", DefaultBiomeHandler().javaClass, 0..0, 0..0, 0..0, setOf())

    fun evaluate(factors: Vector3f): Biome {
        map.values.forEach { v ->
            if (
                v.elevationRange.contains(factors.x.roundToInt()) &&
                v.moistureRange.contains(factors.y.roundToInt()) &&
                v.temperatureRange.contains(factors.z.roundToInt())
            )
                return v
        }

        return nullBiome
    }

}