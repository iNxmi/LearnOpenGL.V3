package com.nami.world.resources.biome.handlers

import com.nami.world.World
import com.nami.world.resources.biome.BiomeListener
import com.nami.world.resources.block.Block
import org.joml.Vector3f
import org.joml.Vector3i

class DefaultBiomeHandler : BiomeListener {

    override fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): Block? {
        return null
    }

}