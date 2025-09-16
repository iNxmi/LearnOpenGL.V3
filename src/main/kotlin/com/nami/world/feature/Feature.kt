package com.nami.world.feature

import com.nami.world.resources.block.Block
import org.joml.Vector3i

abstract class Feature {

    abstract fun shouldGenerate(): Boolean

    abstract fun generate(
        elevation: Float,
        moisture: Float,
        temperature: Float,
        seed: Long = System.currentTimeMillis()
    ): Map<Vector3i, Block>

}