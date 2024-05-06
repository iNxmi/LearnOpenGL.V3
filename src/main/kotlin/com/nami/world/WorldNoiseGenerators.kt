package com.nami.world

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator
import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseGenerator
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction
import de.articdive.jnoise.pipeline.JNoise
import kotlin.random.Random

class WorldNoiseGenerators(seed: Long) {

    val elevationNoise =
        JNoise.newBuilder()
            .fastSimplex(
                FastSimplexNoiseGenerator.newBuilder().setSeed(seed).build()
            )
            .octavate(6, 0.5, 2.5, FractalFunction.FBM, false)
            .scale(1 / 2048.0)
            .addModifier { v -> (v + 1) / 2.0 }
            .clamp(0.0, 1.0)
            .build()

    val moistureNoise = JNoise.newBuilder()
        .fastSimplex(
            FastSimplexNoiseGenerator.newBuilder().setSeed(seed + 1).build()
        )
        .octavate(6, 0.5, 3.0, FractalFunction.FBM, false)
        .scale(1 / 3072.0)
        .addModifier { v -> (v + 1) / 2.0 }
        .clamp(0.0, 1.0)
        .build()

    val temperatureNoise = JNoise.newBuilder()
        .fastSimplex(
            FastSimplexNoiseGenerator.newBuilder().setSeed(seed + 2).build()
        )
        .octavate(6, 0.5, 4.0, FractalFunction.FBM, false)
        .scale(1 / 4096.0)
        .addModifier { v -> (v + 1) / 2.0 }
        .clamp(0.0, 1.0)
        .build()

    val caveNoise = JNoise.newBuilder()
        .worley(
            WorleyNoiseGenerator.newBuilder().setSeed(seed + 3).build()
        )
        .octavate(3, 0.5, 2.0, FractalFunction.FBM, false)
        .scale(0.05)
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