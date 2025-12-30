package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockIce
import com.nami.world.block.blocks.BlockSnow
import com.nami.world.block.blocks.BlockStone
import org.joml.Vector3i
import kotlin.math.roundToInt

object BiomeSpruceForest : Biome(id = "spruce_forest") {

    override val elevation = 67f..256f
    override val moisture = 0f..100f
    override val temperature = -25f..0f

    override fun generate(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height - 4).contains(y))
            return BlockStone

        if ((height - 4 until height).contains(y))
            return if (temperature < -5) BlockIce else BlockSnow

        return null
    }

}