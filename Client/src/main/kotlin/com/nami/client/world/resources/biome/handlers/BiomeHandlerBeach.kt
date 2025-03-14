package com.nami.client.world.resources.biome.handlers

import com.nami.client.resources.Resources
import com.nami.client.world.World
import com.nami.client.world.resources.biome.BiomeListener
import com.nami.client.world.resources.block.Block
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.roundToInt

class BiomeHandlerBeach : BiomeListener {

    override fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): Block? {
        val y = position.y

        val height = factors.x.roundToInt()
        if ((0 until height - 3).contains(y))
            return Resources.BLOCK.get("stone")

        if ((height - 3 until height).contains(y))
            return Resources.BLOCK.get("sand")

        return null
    }

}