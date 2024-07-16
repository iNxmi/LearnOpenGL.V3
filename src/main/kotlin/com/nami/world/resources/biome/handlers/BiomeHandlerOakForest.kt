package com.nami.world.resources.biome.handlers

import com.nami.world.World
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.roundToInt

class BiomeHandlerOakForest : com.nami.world.resources.biome.BiomeListener {
    override fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): String? {
        val y = position.y

        val height = factors.x.roundToInt()
        if ((0 until height - 4).contains(y))
            return "stone"

        if (factors.z > 0) {
            if ((height - 4 until height - 1).contains(y))
                return "dirt"

            if ((height - 1 until height).contains(y))
                return "grass"
        } else {
            if ((height - 4 until height).contains(y))
                return "gravel"
        }

        return null
    }

}