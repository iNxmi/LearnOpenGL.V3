package com.nami.world.feature

import com.nami.random
import com.nami.resources.Resources
import com.nami.world.resources.block.Block
import org.joml.Vector3i

object FeatureCactus : Feature() {

    override fun shouldGenerate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun generate(
        elevation: Float,
        moisture: Float,
        temperature: Float
    ): Map<Vector3i, Block> {
        val blocks = mutableMapOf<Vector3i, Block>()

        val height = Int.random(2..5)
        for (y in 0 until height)
            blocks[Vector3i(0, y, 0)] = Resources.BLOCK.get("cactus")

        return blocks
    }


}