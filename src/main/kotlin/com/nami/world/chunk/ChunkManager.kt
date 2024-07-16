package com.nami.world.chunk

import com.nami.scene.SceneTime
import com.nami.world.World
import com.nami.world.block.Block
import com.nami.world.player.Player
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_CULL_FACE
import java.util.*

class ChunkManager(val world: World) {

    val chunks = mutableMapOf<Vector3i, Chunk>()
    val generator = ChunkGenerator(world, 8)
    val meshGenerator = ChunkMeshGenerator(world, this, 1f)

    fun setChunk(position: Vector3i, chunk: Chunk) {
        chunks[position] = chunk
    }

    fun getByChunkPosition(position: Vector3i): Chunk? {
        return chunks[position]
    }

    fun getByBlockPosition(position: Vector3i): Chunk? {
        return getByChunkPosition(
            Vector3i(
                position.x / Chunk.SIZE.x,
                position.y / Chunk.SIZE.y,
                position.z / Chunk.SIZE.z
            )
        )
    }

    fun update(time: SceneTime, player: Player, chunkRadius: Int) {
        for (z in -chunkRadius..chunkRadius)
            for (y in -chunkRadius..chunkRadius)
                for (x in -chunkRadius..chunkRadius) {
                    if (x * x + y * y + z * z > chunkRadius * chunkRadius)
                        continue

                    val pos = Vector3i(
                        player.transform.position.x.toInt() / Chunk.SIZE.x + x,
                        player.transform.position.y.toInt() / Chunk.SIZE.y + y,
                        player.transform.position.z.toInt() / Chunk.SIZE.z + z
                    )

                    if (!(0 until world.size.x).contains(pos.x)) continue
                    if (!(0 until world.size.y).contains(pos.y)) continue
                    if (!(0 until world.size.z).contains(pos.z)) continue

                    if (!chunks.containsKey(pos))
                        generator.addToQueue(pos)

                    getByChunkPosition(pos)?.update()
                }

        generator.update()
        meshGenerator.update()
    }

    fun render(time: SceneTime, player: Player, chunkRadius: Int) {
        val chunks = TreeMap<Float, Chunk>()
        for (z in -chunkRadius..chunkRadius)
            for (y in -chunkRadius..chunkRadius)
                for (x in -chunkRadius..chunkRadius)
                    if (x * x + y * y + z * z <= chunkRadius * chunkRadius) {
                        val chunkPosition = Vector3i(
                            player.transform.position.x.toInt() / Chunk.SIZE.x + x,
                            player.transform.position.y.toInt() / Chunk.SIZE.y + y,
                            player.transform.position.z.toInt() / Chunk.SIZE.z + z
                        )

                        val chunk = getByChunkPosition(chunkPosition) ?: continue

                        val distance =
                            Vector3f(chunkPosition).mul(Vector3f(Chunk.SIZE)).add(Vector3f(Chunk.SIZE).div(2.0f))
                                .sub(player.transform.position).length()
                        chunks[distance] = chunk
                    }

        chunks.forEach { (_, chunk) -> chunk.render(player, time, Block.Layer.SOLID) }
        chunks.forEach { (_, chunk) ->
            chunk.render(player, time, Block.Layer.TRANSPARENT)
            chunk.render(player, time, Block.Layer.FOLIAGE)
            chunk.render(player, time, Block.Layer.FLUID)
        }
    }

}