package com.nami.world.feature.features

import com.nami.extension.next
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockCactus
import com.nami.world.feature.Feature
import org.joml.Vector3i
import kotlin.random.Random

object FeatureCactus : Feature(id = "cactus") {

    override fun generate(
        elevation: Float,
        moisture: Float,
        temperature: Float,
        seed: Long
    ): Map<Vector3i, Block> {
        val random = Random(seed)
        val blocks = mutableMapOf<Vector3i, Block>()

        val height = random.next(2..5)
        for (y in 0 until height)
            blocks[Vector3i(0, y, 0)] = BlockCactus

        return blocks
    }


}