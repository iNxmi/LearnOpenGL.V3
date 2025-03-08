package com.nami.world.resources.biome

import com.nami.resources.Resources
import com.nami.world.World
import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction
import de.articdive.jnoise.pipeline.JNoise
import org.joml.Vector3f
import org.joml.Vector3i
import java.util.concurrent.ConcurrentHashMap

class BiomeManager(
    val world: World
)  {

    private val biomes = ConcurrentHashMap<Vector3i, Biome.Instance>()

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

        val factors = Vector3f(elevation, moisture, temperature)
        val biome = Resources.BIOME.evaluate(factors) ?: return
//        val biome = Resource.BIOME.get("jungle_forest")
        biomes[position] = biome.create(world, position, factors)
    }

    fun setBiome(position: Vector3i, biome: Biome.Instance) {
        biomes[position] = biome
    }

    fun setBiomes(map: Map<Vector3i, Biome.Instance>) {
        biomes.putAll(map)
    }

    fun getBiome(position: Vector3i): Biome.Instance? {
        return biomes[position]
    }

}