package com.nami.world.biome.handlers

import com.nami.world.World
import com.nami.world.biome.BiomeListener
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.roundToInt

class BiomeHandlerJungleForest : BiomeListener {

    override fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): String? {
        val y = position.y

        val height = factors.x.roundToInt()
        if ((0 until height - 4).contains(y))
            return "stone"

        if ((height - 4 until height - 1).contains(y))
            return "dirt"

        if ((height - 1 until height).contains(y))
            return "grass"

        return null
    }

}