package com.nami.world.block

import com.nami.resources.Resource
import com.nami.world.World
import com.nami.world.chunk.Chunk
import org.joml.Vector2i
import org.joml.Vector3i
import java.util.concurrent.ConcurrentHashMap

class BlockManager(val world: World) {

    val blocks = ConcurrentHashMap<Vector3i, Block.Instance?>()
    val faces = mutableMapOf<Block.Layer, ConcurrentHashMap<Vector3i, Array<Face>>>()

    init {
        Block.Layer.values().forEach { faces[it] = ConcurrentHashMap() }
    }

    fun getHeight(position: Vector2i, start: Int, types: List<Block.Layer>): Int {
        for (y in start downTo 0) {
            val block = getBlock(Vector3i(position.x, y, position.y)) ?: continue

            if (types.contains(block.template.layer))
                return y
        }

        return 0
    }

    private fun updateFaces(position: Vector3i) {
        faces[Block.Layer.FLUID]!!.remove(position)
        faces[Block.Layer.SOLID]!!.remove(position)
        faces[Block.Layer.FOLIAGE]!!.remove(position)

        val block = getBlock(position) ?: return

        val list = mutableListOf<Face>()

        var adjBlock: Block? = getBlock(Vector3i(position).add(1, 0, 0))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer) list.add(Face.EAST)

        adjBlock = getBlock(Vector3i(position).add(-1, 0, 0))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer) list.add(Face.WEST)

        adjBlock = getBlock(Vector3i(position).add(0, 1, 0))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer) list.add(Face.TOP)

        adjBlock = getBlock(Vector3i(position).add(0, -1, 0))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer) list.add(Face.BOTTOM)

        adjBlock = getBlock(Vector3i(position).add(0, 0, 1))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer) list.add(Face.SOUTH)

        adjBlock = getBlock(Vector3i(position).add(0, 0, -1))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer) list.add(Face.NORTH)

        if (list.isEmpty())
            return

        val register = faces[block.template.layer]!!
        register[position] = list.toTypedArray()
    }

    fun getBlock(position: Vector3i): Block.Instance? {
        return blocks[position]
    }

    fun setBlock(position: Vector3i, id: String?, update: Boolean = true): Boolean {
        if (id == null)
            blocks.remove(position)
        else
            blocks[position] = Resource.BLOCK.get(id).create(world, position)

        updateFaces(position)
        updateFaces(Vector3i(position).add(1, 0, 0))
        updateFaces(Vector3i(position).add(-1, 0, 0))
        updateFaces(Vector3i(position).add(0, 1, 0))
        updateFaces(Vector3i(position).add(0, -1, 0))
        updateFaces(Vector3i(position).add(0, 0, 1))
        updateFaces(Vector3i(position).add(0, 0, -1))

        if (!update)
            return true

        world.chunkManager.getByBlockPosition(position)?.updateMesh()

        if (position.x / Chunk.SIZE.x <= 0)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(-1, 0, 0))?.updateMesh()
        if (position.x / Chunk.SIZE.x >= Chunk.SIZE.x - 1)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(1, 0, 0))?.updateMesh()

        if (position.y / Chunk.SIZE.y <= 0)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(0, -1, 0))?.updateMesh()
        if (position.y / Chunk.SIZE.y >= Chunk.SIZE.y - 1)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(0, 1, 0))?.updateMesh()

        if (position.z / Chunk.SIZE.z <= 0)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(0, 0, -1))?.updateMesh()
        if (position.z / Chunk.SIZE.z >= Chunk.SIZE.z - 1)
            world.chunkManager.getByBlockPosition(Vector3i(position).add(0, 0, 1))?.updateMesh()

        return true
    }

}