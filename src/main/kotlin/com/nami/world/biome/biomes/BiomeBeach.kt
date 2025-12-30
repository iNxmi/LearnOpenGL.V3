package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockSand
import com.nami.world.block.blocks.BlockStone
import org.joml.Vector3i
import kotlin.math.roundToInt

object BiomeBeach : Biome(id = "beach") {

    override val elevation = 64f..67f
    override val moisture = 0f..100f
    override val temperature = -25f..50f

    override fun generate(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? {
        val height = elevation.roundToInt()
        return when (position.y) {
            in (0 until height - 3) -> BlockStone
            in (height - 3 until height) -> BlockSand
            else -> null
        }
    }

}