package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockDirt
import com.nami.world.block.blocks.BlockIce
import com.nami.world.block.blocks.BlockSnow
import com.nami.world.block.blocks.BlockStone
import org.joml.Vector3i
import kotlin.math.roundToInt

object BiomeSpruceForest : Biome(id = "spruce_forest") {

    override val density = 67f..256f
    override val moisture = 0f..100f
    override val temperature = -25f..0f

    override fun generate(
        position: Vector3i,
        density: Float,
        densityAbove: Float,
        moisture: Float,
        temperature: Float
    ): Block? {
        //Air
        if (density <= 0)
            return null

        //Surface + Shallow
        if (density < 3.0f)
            if (temperature < -5)
                return BlockIce
            else
                return BlockSnow

        //Other
        return BlockStone
    }

}