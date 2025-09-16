package com.nami.world.feature

import com.nami.world.resources.block.Block
import org.joml.Vector3i

abstract class Feature(val id:String) {

    companion object {

        val set = setOf(
            FeatureBirchTree,
            FeatureCactus,
            FeatureGiantMushroomRed,
            FeatureGiantMushroomYellow,
            FeatureJungleTree,
            FeatureOakTree
        )

        val map = set.associateBy { it.id }

        fun get(id: String) = map[id]

    }

    abstract fun shouldGenerate(): Boolean

    abstract fun generate(
        elevation: Float,
        moisture: Float,
        temperature: Float,
        seed: Long = System.currentTimeMillis()
    ): Map<Vector3i, Block>

}