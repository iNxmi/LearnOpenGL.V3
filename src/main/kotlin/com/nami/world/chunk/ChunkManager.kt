package com.nami.world.chunk

import com.nami.world.World
import org.joml.Vector3i

class ChunkManager(val world: World) {

    private val chunks = mutableMapOf<Vector3i, Chunk>()

    fun generate(position: Vector3i): Chunk? {
        if (!(0 until World.SIZE.x).contains(position.x)) return null
        if (!(0 until World.SIZE.y).contains(position.y)) return null
        if (!(0 until World.SIZE.z).contains(position.z)) return null
        if(chunks.containsKey(position)) return chunks[position]

        val chunk = Chunk(world, Vector3i(position))
        chunks[position] = chunk

        get(Vector3i(position).add(-1, 0, 0))?.updateMesh()
        get(Vector3i(position).add(1, 0, 0))?.updateMesh()

        get(Vector3i(position).add(0, -1, 0))?.updateMesh()
        get(Vector3i(position).add(0, 1, 0))?.updateMesh()

        get(Vector3i(position).add(0, 0, -1))?.updateMesh()
        get(Vector3i(position).add(0, 0, 1))?.updateMesh()

        return chunk
    }

    fun get(position: Vector3i): Chunk? {
        return chunks[position]
    }

}