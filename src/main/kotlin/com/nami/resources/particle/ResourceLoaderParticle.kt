package com.nami.resources.particle

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.snakeToUpperCamelCase
import com.nami.world.particle.Particle
import com.nami.world.particle.ParticleColor
import com.nami.world.particle.ParticleListener
import kotlinx.serialization.json.Json
import org.joml.Vector3f
import java.awt.Color
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderParticle : Resources<Particle>(GamePath.particle, "particle", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Particle {
        val jsonString = Files.readString(path)
        val json = Json.decodeFromString<ParticleJSON>(jsonString)

        val colors = mutableListOf<ParticleColor>()
        json.colors.forEach {
            val colorInt: Int = Integer.parseInt(it.hex, 16)
            val color = Color(colorInt)

            colors.add(
                ParticleColor(
                    Vector3f(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f),
                    it.brightness.min..it.brightness.max
                )
            )
        }

        var handlerClass: Class<*> =
            try {
                Class.forName("com.nami.world.particle.handlers.ParticleHandler${id.snakeToUpperCamelCase()}")
            } catch (e: Exception) {
                Class.forName("com.nami.world.particle.handlers.DefaultParticleHandler")
            }

        return Particle(
            id,
            handlerClass as Class<ParticleListener>,

            json.timeInSeconds.min..json.timeInSeconds.max,
            json.scale.min..json.scale.max,
            colors,
        )
    }

    override fun onLoadCompleted() {

    }

}