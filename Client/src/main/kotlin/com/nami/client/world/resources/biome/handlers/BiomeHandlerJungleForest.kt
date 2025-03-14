package com.nami.client.world.resources.biome.handlers

import com.nami.client.resources.Resources
import com.nami.client.world.World
import com.nami.client.world.resources.biome.BiomeListener
import com.nami.client.world.resources.block.Block
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.roundToInt

class BiomeHandlerJungleForest : BiomeListener {

    override fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): Block? {
        val y = position.y

        val height = factors.x.roundToInt()
        if ((0 until height - 4).contains(y))
            return Resources.BLOCK.get("stone")

        if ((height - 4 until height - 1).contains(y))
            return Resources.BLOCK.get("dirt")

        if ((height - 1 until height).contains(y))
            return Resources.BLOCK.get("grass")

        return null
    }

}