package com.nami.resources.biome

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.serializer.GlobalJSON
import com.nami.world.resources.biome.Biome
import org.joml.Vector3f
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.roundToInt

class ResourceLoaderBiome : Resources<Biome>(GamePath.biome, "biome", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Biome {
        val jsonString = Files.readString(path)
        val json: Biome.JSON = GlobalJSON.instance.decodeFromString<Biome.JSON>(jsonString)
        return json.create(id)
    }

    override fun onLoadCompleted() {

    }

    fun evaluate(factors: Vector3f): Biome? {
        map.values.forEach { v ->
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