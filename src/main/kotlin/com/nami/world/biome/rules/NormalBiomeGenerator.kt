package com.nami.world.biome.rules

import com.nami.world.biome.BiomeGenerator
import com.nami.world.block.Block
import com.nami.world.block.BlockTemplate
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import kotlin.math.roundToInt

class NormalBiomeGenerator : BiomeGenerator {

    override fun name(): String {
        return "Normal"
    }

    override fun generateColumn(factors: Vector3f, position: Vector2i): Map<Int, BlockTemplate> {
        val map = mutableMapOf<Int, BlockTemplate>()

        val height = factors.x.roundToInt()
        for (y in 0 until height)
            map[y] = if (height - y > 4) {
                Block.stone
            } else if (height - y > 1) {
                Block.dirt
            } else {
                Block.grass
            }

        return map
    }

}