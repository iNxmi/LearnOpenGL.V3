package com.nami.world.biome

import com.nami.resources.Resources
import com.nami.world.resources.block.Block
import org.joml.Vector3i
import kotlin.math.roundToInt

internal object BiomeInvalid : Biome(id = "invalid") {
    override val elevation = 0f..0f
    override val moisture = 0f..0f
    override val temperature = 0f..0f

    override fun generateBlock(
        position: Vector3i,
        elevation: Float,
        moisture: Float,
        temperature: Float
    ): Block? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height).contains(y))
            return Resources.BLOCK.get("invalid")

        return null
    }
}