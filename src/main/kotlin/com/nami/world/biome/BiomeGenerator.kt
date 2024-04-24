package com.nami.world.biome

import com.nami.world.block.BlockTemplate
import org.joml.Vector2i
import org.joml.Vector3f

interface BiomeGenerator {

    fun name(): String
    fun generateColumn(factors: Vector3f, position: Vector2i): Map<Int, BlockTemplate>

}