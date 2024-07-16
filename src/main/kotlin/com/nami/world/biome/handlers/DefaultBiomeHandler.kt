package com.nami.world.biome.handlers

import com.nami.world.World
import com.nami.world.biome.BiomeListener
import org.joml.Vector3f
import org.joml.Vector3i

class DefaultBiomeHandler : BiomeListener {

    override fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): String? {
        return null
    }

}