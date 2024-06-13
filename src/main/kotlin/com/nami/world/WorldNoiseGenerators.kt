package com.nami.world

import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator
import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseGenerator
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction
import de.articdive.jnoise.pipeline.JNoise
import kotlin.random.Random

class WorldNoiseGenerators(seed: Long) {

    val caveNoise = JNoise.newBuilder()
        .worley(
            WorleyNoiseGenerator.newBuilder().setSeed(seed + 3).build()
        )
        .octavate(4, 0.5, 3.0, FractalFunction.FBM, false)
        .scale(1.0 / 512.0)
        .addModifier { v -> (v + 1) / 2.0 }
        .clamp(0.0, 1.0)
        .build()

    val treeNoise = JNoise.newBuilder()
        .superSimplex(
            SuperSimplexNoiseGenerator.newBuilder().setSeed(seed + 4).build()
        )
        .scale(1024.0)
        .addModifier { v -> (v + 1) / 2.0 }
        .clamp(0.0, 1.0)
        .build()
    val treeRandom = Random(seed)

}