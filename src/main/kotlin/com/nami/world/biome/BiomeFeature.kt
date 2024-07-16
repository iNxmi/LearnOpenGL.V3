package com.nami.world.biome

import com.nami.world.block.Block
import com.nami.world.feature.Feature
import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise

data class BiomeFeature(
    val feature: Feature,
    val base: Set<Block>,
    val scale: Float,
    val radius: Int
) {

    fun noise(seed: Long): JNoise {
        return JNoise.newBuilder()
            .superSimplex(
                SuperSimplexNoiseGenerator.newBuilder().setSeed(seed + feature.id.hashCode()).build()
            )
            .scale(scale.toDouble())
            .addModifier { v -> (v + 1) / 2.0 }
            .clamp(0.0, 1.0)
            .build()
    }

}