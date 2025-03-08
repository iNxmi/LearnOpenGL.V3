package com.nami.resources.particle

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.world.resources.particle.Particle
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderParticle : Resources<Particle>(GamePath.particle, "particle", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Particle {
        val jsonString = Files.readString(path)
        val json = Json.decodeFromString<Particle.JSON>(jsonString)

        return json.create(id)
    }

    override fun onLoadCompleted() {

    }

}