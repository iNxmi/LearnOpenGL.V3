package com.nami.resources.biome

import com.nami.resources.GamePath
import com.nami.resources.Resource
import com.nami.world.biome.Biome
import com.nami.world.biome.BiomeListener
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.joml.Vector3f
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.roundToInt

class ResourceBiome : Resource<Biome>(GamePath.biomes, arrayOf("json")) {

    @Serializable
    data class RangeJSON(
        val min: Int,
        val max: Int
    )

    @Serializable
    data class BiomeJSON(
        val id: String,
        val elevation: RangeJSON,
        val moisture: RangeJSON,
        val temperature: RangeJSON,
        val handler: String? = null
    )

    override fun onLoad(path: Path): Biome {
        val jsonString = Files.readString(path)
        val json: BiomeJSON = Json.decodeFromString<BiomeJSON>(jsonString)

        var handler: BiomeListener? =
            Class.forName(json.handler ?: "com.nami.world.biome.handlers.BiomeHandlerID${json.id}")
                .getDeclaredConstructor().newInstance() as BiomeListener


        return Biome(
            json.id,
            IntRange(json.elevation.min, json.elevation.max),
            IntRange(json.moisture.min, json.moisture.max),
            IntRange(json.temperature.min, json.temperature.max),
            handler
        )
    }

    override fun onLoadCompleted() {

    }

    fun evaluate(factors: Vector3f): Biome? {
        map.forEach { (k, v) ->
            if (
                v.elevationRange.contains(factors.x.roundToInt()) &&
                v.moistureRange.contains(factors.y.roundToInt()) &&
                v.temperatureRange.contains(factors.z.roundToInt())
            )
                return v
        }

        return null
    }

}