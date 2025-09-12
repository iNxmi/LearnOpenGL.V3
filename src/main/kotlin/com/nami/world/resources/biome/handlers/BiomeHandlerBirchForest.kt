package com.nami.world.resources.biome.handlers

import com.nami.resources.Resources
import com.nami.world.World
import com.nami.world.resources.biome.BiomeListener
import com.nami.world.resources.block.Block
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.roundToInt

class BiomeHandlerBirchForest : BiomeListener {
    override fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): Block? {
        val y = position.y

        val height = factors.x.roundToInt()
        if ((0 until height - 4).contains(y))
            return Resources.BLOCK.get("stone")

        if (factors.z > 0) {
            if ((height - 4 until height - 1).contains(y))
                return Resources.BLOCK.get("dirt")

            if ((height - 1 until height).contains(y))
                return Resources.BLOCK.get("podzol")
        } else {
            if ((height - 4 until height).contains(y))
                return Resources.BLOCK.get("gravel")
        }

        return null
    }

}