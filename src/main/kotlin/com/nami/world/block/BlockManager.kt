package com.nami.world.block

import com.nami.world.World
import com.nami.world.chunk.Chunk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joml.Vector2i
import org.joml.Vector3i
import java.util.concurrent.ConcurrentHashMap

class BlockManager(val world: World) {

    val blocks = ConcurrentHashMap<Vector3i, Block?>()

    fun getHeight(position: Vector2i, start: Int): Int {
        for (y in start downTo 0)
            if (getBlock(Vector3i(position.x, y, position.y)) != null)
                return y

        return 0
    }

    fun getBlock(position: Vector3i): Block? {
        return blocks[position]
    }

    fun setBlock(position: Vector3i, block: Block.Template?, update: Boolean = true): Boolean {
        if (block == null)
            blocks.remove(position)
        else
            blocks[position] = block.create(position)

        if (!update)
            return true

        world.chunkManager.getByBlockPosition(position)?.updateMesh()

        if (position.x <= 0)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(-1, 0, 0))?.updateMesh()
        if (position.x >= Chunk.SIZE.x - 1)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(1, 0, 0))?.updateMesh()

        if (position.y <= 0)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(0, -1, 0))?.updateMesh()
        if (position.y >= Chunk.SIZE.y - 1)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(0, 1, 0))?.updateMesh()

        if (position.z <= 0)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(0, 0, -1))?.updateMesh()
        if (position.z >= Chunk.SIZE.z - 1)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(0, 0, 1))?.updateMesh()

        return true
    }

}