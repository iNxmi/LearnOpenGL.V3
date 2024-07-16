package com.nami.world.resources.biome.handlers

import com.nami.world.World
import org.joml.Vector3f
import org.joml.Vector3i

class DefaultBiomeHandler : com.nami.world.resources.biome.BiomeListener {

    override fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): String? {
        return null
    }

}