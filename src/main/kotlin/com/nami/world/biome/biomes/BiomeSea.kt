package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockGravel
import com.nami.world.block.blocks.BlockSand
import com.nami.world.block.blocks.BlockStone
import com.nami.world.block.blocks.BlockWater
import org.joml.Vector3i
import kotlin.math.roundToInt

object BiomeSea : Biome(id = "sea") {

    override val elevation = 0f..64f
    override val moisture = 0f..100f
    override val temperature = -25f..50f

    override fun generate(
        position: Vector3i,
        density: Float,
        densityAbove: Float,
        moisture: Float,
        temperature: Float
    ): Block? {
        //Air / Water
        if (density <= 0f) {
            if (position.y <= 64) {
                return BlockWater
            } else {
                return null
            }
        }

        //Surface
        if (densityAbove <= 0f) {
            if (temperature <= 10f) {
                return BlockGravel
            } else {
                return BlockSand
            }
        }

        //Shallow
        if (density < 4.0f) {
            if (temperature <= 10f) {
                return BlockGravel
            } else {
                return BlockSand
            }
        }

        //Other
        return BlockStone
    }

}