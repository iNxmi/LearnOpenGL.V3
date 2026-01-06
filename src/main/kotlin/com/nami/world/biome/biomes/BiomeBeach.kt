package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockGravel
import com.nami.world.block.blocks.BlockSand
import com.nami.world.block.blocks.BlockStone
import org.joml.Vector3i
import kotlin.math.roundToInt

object BiomeBeach : Biome(id = "beach") {

    override val elevation = 64f..67f
    override val moisture = 0f..100f
    override val temperature = -25f..50f

    override fun generate(
        position: Vector3i,
        density: Float,
        densityAbove: Float,
        moisture: Float,
        temperature: Float
    ): Block? {

        //Air
        if (density <= 0f)
            return null

        //Surface
        if (densityAbove <= 0f)
            if (temperature <= 10f) {
                return BlockGravel
            } else {
                return BlockSand
            }

        //Shallow
        if (density < 4.0f)
            if (temperature <= 10f) {
                return BlockGravel
            } else {
                return BlockSand
            }

        //Other
        return BlockStone
    }

}