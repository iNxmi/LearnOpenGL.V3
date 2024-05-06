package com.nami.world.biome.generator

import com.nami.world.block.Block
import org.joml.Vector3f
import org.joml.Vector3i

interface BiomeGenerator {
    fun generate(factors: Vector3f, position: Vector3i):  Block.Template?

}