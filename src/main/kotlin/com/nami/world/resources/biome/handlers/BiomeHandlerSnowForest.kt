package com.nami.world.resources.biome.handlers

import com.nami.world.World
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.roundToInt

class BiomeHandlerSnowForest : com.nami.world.resources.biome.BiomeListener {
    override fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): String? {
        val y = position.y

        val height = factors.x.roundToInt()
        if ((0 until height - 4).contains(y))
            return "stone"

        if ((height - 4 until height).contains(y))
            return if (factors.z < -5) "ice" else "snow"

        return null
    }

}