package com.nami.world

import com.google.common.collect.TreeMultimap
import com.nami.Time
import com.nami.engine.graphics.Graphics
import com.nami.extension.div
import com.nami.extension.minus
import com.nami.extension.plus
import com.nami.extension.times
import com.nami.world.biome.BiomeGenerator
import com.nami.world.block.Block
import com.nami.world.block.BlockManagerSlow
import com.nami.world.block.Layer
import com.nami.world.chunk.Chunk
import mu.KotlinLogging
import org.joml.Vector3f
import org.joml.Vector3i
import org.joml.Vector4f
import org.lwjgl.opengl.GL11.GL_CULL_FACE
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.opengl.GL33.glClearColor

class World(
    val size: Vector3i,
    val seed: Long
) {

    private val logger = KotlinLogging.logger {}

    val time = Time()

    val blockManager = BlockManagerSlow(this)

    val chunks = mutableMapOf<Vector3i, Chunk>()

    val biomeGenerator = BiomeGenerator(seed)

    val player = Player()

    private val radius = 6

    fun render(graphics: Graphics) {
        time.update()

        graphics.clearColor = Vector4f(
            52.0f / 255.0f,
            146.0f / 255.0f,
            235.0f / 255.0f,
            1.0f
        ).mul(0.50f)

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

                    chunkPositions.add(chunkPosition)
                }

//        unload unused chunks immediately after not being in range
//        chunks.entries.removeIf { it.key !in chunkPositions }

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
            val worldPositionCamera = player.camera.position
            val distance = worldPositionChunk.distanceSquared(worldPositionCamera)

            sorted.put(distance, chunk)
        }

        for (distance in sorted.keySet().descendingSet())
            for (chunk in sorted.get(distance).descendingSet())
                chunk.render(time, player, Layer.SOLID)

        for (distance in sorted.keySet().descendingSet())
            for (chunk in sorted.get(distance).descendingSet()) {
                chunk.render(time, player, Layer.TRANSPARENT)
                chunk.render(time, player, Layer.FLUID)
            }
    }

    fun getGlobalBlock(position: Vector3i): Block? {
        val chunkPosition = position / Chunk.SIZE
        val chunkLocalBlockPosition = position - (chunkPosition * Chunk.SIZE)
        return chunks[chunkPosition]?.blocks[chunkLocalBlockPosition]
    }

}