package com.nami.world.feature

import com.nami.world.block.Block
import com.nami.world.feature.features.*
import com.nami.world.recipe.Recipe
import de.articdive.jnoise.pipeline.JNoise
import mu.KotlinLogging
import org.joml.Vector3i

abstract class Feature(val id: String) {

    companion object {
        val set by lazy {
            setOf(
                FeatureBirchTree,
                FeatureCactus,
                FeatureGiantMushroomRed,
                FeatureGiantMushroomYellow,
                FeatureJungleTree,
                FeatureOakTree
            )
        }
        val map by lazy { set.associateBy { it.id } }

        fun get(id: String) = map[id]
    }

    abstract fun generate(
        elevation: Float,
        moisture: Float,
        temperature: Float,
        seed: Long = System.currentTimeMillis()
    ): Map<Vector3i, Block>

}