package com.nami.world.biome

import com.nami.world.World
import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction
import de.articdive.jnoise.pipeline.JNoise
import org.joml.Vector3f
import org.joml.Vector3i
import java.util.concurrent.ConcurrentHashMap

class iomeManager(
    val world: World
) {

    private val biomes = ConcurrentHashMap<Vector3i, Vector3f>()

    private val elevation =
        JNoise.newBuilder()
            .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(world.seed).build())
            .octavate(6, 0.5, 2.5, FractalFunction.FBM, false)
            .scale(1 / 4098.0)
            .addModifier { v -> ((v + 1) / 2.0) * 256 }
            .clamp(0.0, 256.0)
            .build()

    private val moisture = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(world.seed + 1).build())
        .octavate(6, 0.5, 4.0, FractalFunction.FBM, false)
        .scale(1 / 2048.0)
        .addModifier { v -> ((v + 1) / 2.0) * 100 }
        .clamp(0.0, 100.0)
        .build()

    private val temperature = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(world.seed + 2).build())
        .octavate(6, 0.5, 4.0, FractalFunction.FBM, false)
        .scale(1 / 2048.0)
        .addModifier { v -> ((v + 1) / 2.0) * (50 + 25) - 25 }
        .clamp(-25.0, 50.0)
        .build()

    fun generate(position: Vector3i) {
        if (biomes.containsKey(position))
            return

        val elevation = elevation.evaluateNoise(
            position.x.toDouble(),
            position.z.toDouble()
        ).toFloat()

        val moisture = moisture.evaluateNoise(
            position.x.toDouble(),
            position.y.toDouble(),
            position.z.toDouble()
        ).toFloat()

        val temperature = temperature.evaluateNoise(
            position.x.toDouble(),
            position.y.toDouble(),
            position.z.toDouble()
        ).toFloat()

        biomes[position] = Vector3f(elevation, moisture, temperature)
    }

    fun setBiomeFactor(position: Vector3i, factors: Vector3f) = biomes.set(position, factors)
    fun setBiomeFactors(map: Map<Vector3i, Vector3f>) = biomes.putAll(map)
    fun getBiomeFactors(position: Vector3i) = biomes[position]!!


}