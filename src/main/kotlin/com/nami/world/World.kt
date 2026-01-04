package com.nami.world

import com.google.common.collect.TreeMultimap
import com.nami.Time
import com.nami.extension.div
import com.nami.extension.minus
import com.nami.extension.plus
import com.nami.extension.times
import com.nami.world.block.BlockManagerSlow
import com.nami.world.block.Layer
import com.nami.world.chunk.Chunk
import com.nami.world.chunk.Voxel
import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.modules.octavation.fractal_functions.FractalFunction
import de.articdive.jnoise.pipeline.JNoise
import mu.KotlinLogging
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.GL11.GL_CULL_FACE
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.opengl.GL33.glClearColor
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.math.abs

class World(
    val size: Vector3i,
    val seed: Long,
    val waterLevel: Int
) {

    private val log = KotlinLogging.logger {}

    companion object {
        val ELEVATION_RANGE = 0.0..256.0
        val MOISTURE_RANGE = 0.0..100.0
        val TEMPERATURE_RANGE = -25.0..50.0
    }

//    val scale = 4096.0
    val scale = 1024.0

    val elevation: JNoise = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(seed).build())
        .octavate(6, 0.5, 2.5, FractalFunction.FBM, false)
        .scale(1.0 / (2.0 * scale))
        .addModifier { v -> ((v + 1) / 2.0) * 256 }
        .clamp(ELEVATION_RANGE.start, ELEVATION_RANGE.endInclusive)
        .build()

    val moisture: JNoise = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(seed + 1).build())
        .octavate(6, 0.5, 4.0, FractalFunction.FBM, false)
        .scale(1.0 / scale)
        .addModifier { v -> ((v + 1) / 2.0) * MOISTURE_RANGE.endInclusive }
        .clamp(MOISTURE_RANGE.start, MOISTURE_RANGE.endInclusive)
        .build()

    val temperature: JNoise = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(seed + 2).build())
        .octavate(6, 0.5, 4.0, FractalFunction.FBM, false)
        .scale(1.0 /  scale)
        .addModifier { v -> ((v + 1) / 2.0) * (abs(TEMPERATURE_RANGE.start) + TEMPERATURE_RANGE.endInclusive) - abs(TEMPERATURE_RANGE.start) }
        .clamp(TEMPERATURE_RANGE.start, TEMPERATURE_RANGE.endInclusive)
        .build()

    val time = Time()

    val blockManager = BlockManagerSlow(this)

    val chunks = mutableMapOf<Vector3i, Chunk>()

    val player = Player()

    private val radius = 6

    fun update() {
        time.update()

        val color = Vector3f(52f / 255f, 146f / 255f, 235f / 255f).mul(1f)
        glClearColor(color.x, color.y, color.z, 1.0f)

        player.update(this)

        val chunkPositions = mutableSetOf<Vector3i>()
        for (z in -radius..radius)
            for (y in -radius..radius)
                for (x in -radius..radius) {
                    if (x * x + y * y + z * z > radius * radius)
                        continue

                    val chunkPosition = player.getChunkPosition() + Vector3i(x, y, z)

                    if (!(0 until size.x).contains(chunkPosition.x)) continue
                    if (!(0 until size.y).contains(chunkPosition.y)) continue
                    if (!(0 until size.z).contains(chunkPosition.z)) continue

                    if (!chunks.containsKey(chunkPosition)) {
                        val chunk = Chunk(this, chunkPosition)
                        chunks[chunkPosition] = chunk

                        chunks[chunkPosition + Vector3i(1, 0, 0)]?.generateMesh()
                        chunks[chunkPosition + Vector3i(-1, 0, 0)]?.generateMesh()
                        chunks[chunkPosition + Vector3i(0, 1, 0)]?.generateMesh()
                        chunks[chunkPosition + Vector3i(0, -1, 0)]?.generateMesh()
                        chunks[chunkPosition + Vector3i(0, 0, 1)]?.generateMesh()
                        chunks[chunkPosition + Vector3i(0, 0, -1)]?.generateMesh()
                    }

                    chunks[chunkPosition]!!.update()
                    chunkPositions.add(chunkPosition)
                }

//        unload unused chunks immediately after not being in range
//        chunks.entries.removeIf { it.key !in chunkPositions }
    }

    fun render() {
        val sorted = TreeMultimap.create<Float, Chunk>(
            naturalOrder(),
            compareBy<Chunk>(
                { it.position.x },
                { it.position.y },
                { it.position.z }
            )
        )

        // Add  hunks in a specific radius
//        for (z in -radius..radius)
//            for (y in -radius..radius)
//                for (x in -radius..radius) {
//                    if (x * x + y * y + z * z > radius * radius)
//                        continue
//
//                    val chunkPosition = player.getChunkPosition() + Vector3i(x, y, z)
//                    val chunk = chunks[chunkPosition] ?: continue
//
//                    val distance = Vector3f(chunkPosition)
//                        .mul(Vector3f(Chunk.SIZE))
//                        .add(Vector3f(Chunk.SIZE).div(2.0f))
//                        .sub(player.transform.position)
//                        .length()
//
//                    sorted.put(distance, chunk)
//                }

        for ((chunkPosition, chunk) in chunks) {
            val worldPositionChunk =
                Vector3f(chunkPosition).mul(Vector3f(Chunk.SIZE)).add(Vector3f(Chunk.SIZE).div(2.0f))
            val worldPositionCamera = player.camera.transform.position
            val distance = worldPositionChunk.distanceSquared(worldPositionCamera)

            sorted.put(distance, chunk)
        }

        glEnable(GL_CULL_FACE)
        for (distance in sorted.keySet().descendingSet())
            for (chunk in sorted.get(distance).descendingSet())
                chunk.render(time, player, Layer.SOLID)

        for (distance in sorted.keySet().descendingSet())
            for (chunk in sorted.get(distance).descendingSet()) {
                chunk.render(time, player, Layer.TRANSPARENT)
                chunk.render(time, player, Layer.FLUID)
            }
    }

    fun getVoxel(chunkPosition: Vector3i, blockPosition: Vector3i): Voxel? =
        chunks[chunkPosition]?.voxels[blockPosition]

    fun getVoxel(position: Vector3i): Voxel? {
        val chunkPosition = position / Chunk.SIZE
        val chunkLocalBlockPosition = position - (chunkPosition * Chunk.SIZE)
        return getVoxel(chunkPosition, chunkLocalBlockPosition)
    }

}