package com.nami.world.feature

import com.nami.world.block.Block
import com.nami.world.feature.features.FeatureBirchTree
import com.nami.world.feature.features.FeatureCactus
import com.nami.world.feature.features.FeatureGiantMushroomRed
import com.nami.world.feature.features.FeatureGiantMushroomYellow
import com.nami.world.feature.features.FeatureJungleTree
import com.nami.world.feature.features.FeatureOakTree
import org.joml.Vector3i

abstract class Feature(val id: String) {

    companion object {

        val set: Set<Feature> = setOf(
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