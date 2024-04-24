package com.nami.world.biome.rules

import com.nami.world.biome.BiomeGenerator
import com.nami.world.block.Block
import com.nami.world.block.BlockTemplate
import org.joml.Vector2i
import org.joml.Vector3f
import kotlin.math.roundToInt

class DesertBiomeGenerator : BiomeGenerator {
    override fun name(): String {
        return "Desert"
    }

    override fun generateColumn(factors: Vector3f, position: Vector2i): Map<Int, BlockTemplate> {
        val height = factors.x.roundToInt()

        val map = mutableMapOf<Int, BlockTemplate>()
        for(y in 0 until height-3)
            map[y] = Block.stone

        for (y in height-3 until height)
            map[y] = Block.sand

        return map
    }
}