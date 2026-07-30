package com.nami.world.biome

import com.nami.extension.plus
import com.nami.extension.times
import com.nami.world.chunk.Chunk
import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction
import de.articdive.jnoise.pipeline.JNoise
import org.joml.Vector2i
import org.joml.Vector3i
import kotlin.math.abs

class BiomeGenerator(
    val seed: Long,
    val elevationRange: ClosedFloatingPointRange<Double> = 0.0..256.0,
    val moistureRange: ClosedFloatingPointRange<Double> = 0.0..100.0,
    val temperatureRange: ClosedFloatingPointRange<Double> = -25.0..50.0
) {

    private val scale = 4096.0

    private val elevationNoise: JNoise = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(seed).build())
        .octavate(6, 0.5, 2.5, FractalFunction.FBM, false)
        .scale(1.0 / (2.0 * scale))
        .addModifier { v -> ((v + 1) / 2.0) * elevationRange.endInclusive }
        .clamp(elevationRange.start, elevationRange.endInclusive)
        .build()

    private val moistureNoise: JNoise = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(seed + 1).build())
        .octavate(6, 0.5, 4.0, FractalFunction.FBM, false)
        .scale(1.0 / scale)
        .addModifier { v -> ((v + 1) / 2.0) * moistureRange.endInclusive }
        .clamp(moistureRange.start, moistureRange.endInclusive)
        .build()

    private val temperatureNoise: JNoise = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(seed + 2).build())
        .octavate(6, 0.5, 4.0, FractalFunction.FBM, false)
        .scale(1.0 / scale)
        .addModifier { v ->
            ((v + 1) / 2.0) * (abs(temperatureRange.start) + temperatureRange.endInclusive) - abs(temperatureRange.start)
        }
        .clamp(temperatureRange.start, temperatureRange.endInclusive)
        .build()

    private val caveNoise: JNoise = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(seed + 3).build())
        .octavate(4, 0.5, 2.0, FractalFunction.FBM, false)
        .scale(1.0 / 96.0)
        .build()

    fun getElevation(chunk: Chunk, position: Vector2i):Float {
        val value = elevationNoise.evaluateNoise(
            (chunk.position.x * Chunk.SIZE.x + position.x).toDouble(),
            (chunk.position.z * Chunk.SIZE.z + position.y).toDouble()
        ).toFloat()

        return value
    }

    fun getDensity(chunk: Chunk, position: Vector3i): Float {
        val globalPosition = (chunk.position * Chunk.SIZE) + position

        val elevation = getElevation(chunk, Vector2i(position.x, position.z))

        val caveValue = caveNoise.evaluateNoise(
            globalPosition.x.toDouble(),
            globalPosition.y.toDouble(),
            globalPosition.z.toDouble()
        ).toFloat()

        return elevation - globalPosition.y - caveValue
    }

    fun getMoisture(chunk: Chunk, position: Vector3i): Float {
        val globalPosition = (chunk.position * Chunk.SIZE) + position

        val value = moistureNoise.evaluateNoise(
            globalPosition.x.toDouble(),
            globalPosition.y.toDouble(),
            globalPosition.z.toDouble()
        ).toFloat()

        return value
    }

    fun getTemperature(chunk: Chunk, position: Vector3i): Float {
        val globalPosition = (chunk.position * Chunk.SIZE) + position

        val value = temperatureNoise.evaluateNoise(
            globalPosition.x.toDouble(),
            globalPosition.y.toDouble(),
            globalPosition.z.toDouble()
        ).toFloat()

        return value
    }

}