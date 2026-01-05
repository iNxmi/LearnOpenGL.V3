package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockError
import org.joml.Vector3i
import kotlin.math.roundToInt

internal object BiomeError : Biome(id = "error") {

    override val density = 0f..0f
    override val moisture = 0f..0f
    override val temperature = 0f..0f

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

        //Other
        return BlockError
    }

}