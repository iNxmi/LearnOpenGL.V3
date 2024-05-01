package com.nami.world.biome.generator

import com.nami.world.block.Block
import com.nami.world.block.BlockTemplate
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.roundToInt

class SnowBiomeGenerator : BiomeGenerator {

    override fun generate(factors: Vector3f, position: Vector3i): BlockTemplate? {
        val y = position.y

        val height = factors.x.roundToInt()
        if ((0 until height - 4).contains(y))
            return Block.STONE

        if ((height - 4 until height).contains(y))
            return Block.SNOW

        return null
    }

}