package com.nami.world.chunk

import com.nami.world.Player
import com.nami.world.World
import com.nami.world.biome.Biome
import com.nami.world.block.Layer
import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction
import de.articdive.jnoise.pipeline.JNoise
import org.joml.Vector3i

class Chunk(
    val world: World,


    val position: Vector3i
) {

    companion object {
        val SIZE = Vector3i(16, 16, 16)
        val LENGTH = SIZE.x * SIZE.y * SIZE.z
    }

    private val root = world.root.resolve("chunks")
    private val fileName = "${position.x}_${position.y}_${position.z}"

    private val scale = 2.0f

    private val elevation = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(world.seed).build())
        .octavate(6, 0.5, 2.5, FractalFunction.FBM, false)
        .scale(1 / (4098.0 * scale))
        .addModifier { v -> ((v + 1) / 2.0) * 256 }
        .clamp(0.0, 256.0)
        .build()

    private val moisture = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(world.seed + 1).build())
        .octavate(6, 0.5, 4.0, FractalFunction.FBM, false)
        .scale(1 / (2048.0 * scale))
        .addModifier { v -> ((v + 1) / 2.0) * 100 }
        .clamp(0.0, 100.0)
        .build()

    private val temperature = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(world.seed + 2).build())
        .octavate(6, 0.5, 4.0, FractalFunction.FBM, false)
        .scale(1 / (2048.0 * scale))
        .addModifier { v -> ((v + 1) / 2.0) * (50 + 25) - 25 }
        .clamp(-25.0, 50.0)
        .build()

    val voxels = mutableMapOf<Vector3i, Voxel>()
    val meshes = mutableMapOf<Layer, Mesh>()

    init {
        Layer.entries.forEach { meshes[it] = Mesh(this, it) }

        for (z in 0 until SIZE.z)
            for (x in 0 until SIZE.x) {
                val elevation = elevation.evaluateNoise(x.toDouble(), z.toDouble()).toFloat()
                for (y in 0 until SIZE.y) {
                    val position = Vector3i(this@Chunk.position).mul(SIZE).add(x, y, z)

                    val moisture = moisture.evaluateNoise(x.toDouble(), y.toDouble(), z.toDouble()).toFloat()
                    val temperature = temperature.evaluateNoise(x.toDouble(), y.toDouble(), z.toDouble()).toFloat()

                    val biome = Biome.create(position, elevation, moisture, temperature)
                    val block = biome.template.generate(position, elevation, moisture, temperature) ?: continue

                    voxels[position] = Voxel(position, biome, block)
                }
            }
    }

    fun update() {}

    fun render(player: Player, layer: Layer) = meshes[layer]?.render(player, world.time)

    fun generateMesh() = meshes.forEach { it.value.generate() }

    fun uploadMesh() = meshes.forEach { it.value.upload() }

}