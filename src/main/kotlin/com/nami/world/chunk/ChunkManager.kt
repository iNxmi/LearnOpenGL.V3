package com.nami.world.chunk

import com.nami.world.World
import org.joml.Vector2i

class ChunkManager(val world: World, private val generator: ChunkGenerator) {

    val chunks = mutableMapOf<Vector2i, Chunk>()

    fun generate(position: Vector2i): Chunk? {
        if (position.x < 0 || position.x >= World.width || position.y < 0 || position.y >= World.depth)
            return null

        val chunk = Chunk(world, Vector2i(position), generator)
        chunks[position] = chunk

        val chunkNorth = get(Vector2i(position).add(0, -1))
        chunkNorth?.updateMesh()

        val chunkEast = get(Vector2i(position).add(-1, 0))
        chunkEast?.updateMesh()

        val chunkSouth = get(Vector2i(position).add(0, 1))
        chunkSouth?.updateMesh()

        val chunkWest = get(Vector2i(position).add(1, 0))
        chunkWest?.updateMesh()

        return chunk
    }

    fun get(position: Vector2i): Chunk? {
        return chunks[position]
    }

}