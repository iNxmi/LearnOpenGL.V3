package com.nami.world.block

import com.nami.resources.Resources
import com.nami.scene.SceneTime
import com.nami.world.World
import org.joml.Vector2i
import org.joml.Vector3i
import java.util.concurrent.ConcurrentHashMap

class BlockManager(val world: World) {

    val instances = ConcurrentHashMap<Vector3i, Block.Instance?>()
    val faces = mutableMapOf<Block.Layer, ConcurrentHashMap<Vector3i, Array<Face>>>()

    init {
        Block.Layer.values().forEach { faces[it] = ConcurrentHashMap() }
    }

    fun getHeight(position: Vector2i, start: Int, types: Set<Block.Layer>): Int {
        for (y in start downTo 0) {
            val block = getBlock(Vector3i(position.x, y, position.y)) ?: continue

            if (types.contains(block.template.layer))
                return y
        }

        return 0
    }

    private fun updateFaces(position: Vector3i) {
        Block.Layer.values().forEach { faces[it]!!.remove(position) }

        val block = getBlock(position) ?: return

        val list = mutableListOf<Face>()

        var adjBlock: Block? = getBlock(Vector3i(position).add(1, 0, 0))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer || block.template.layer == Block.Layer.FOLIAGE) list.add(Face.EAST)

        adjBlock = getBlock(Vector3i(position).add(-1, 0, 0))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer|| block.template.layer == Block.Layer.FOLIAGE) list.add(Face.WEST)

        adjBlock = getBlock(Vector3i(position).add(0, 1, 0))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer|| block.template.layer == Block.Layer.FOLIAGE) list.add(Face.TOP)

        adjBlock = getBlock(Vector3i(position).add(0, -1, 0))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer|| block.template.layer == Block.Layer.FOLIAGE) list.add(Face.BOTTOM)

        adjBlock = getBlock(Vector3i(position).add(0, 0, 1))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer|| block.template.layer == Block.Layer.FOLIAGE) list.add(Face.SOUTH)

        adjBlock = getBlock(Vector3i(position).add(0, 0, -1))?.template
        if (adjBlock == null || block.template.layer != adjBlock.layer|| block.template.layer == Block.Layer.FOLIAGE) list.add(Face.NORTH)

        if (list.isEmpty())
            return

        val register = faces[block.template.layer]!!
        register[position] = list.toTypedArray()
    }

    fun getBlock(position: Vector3i): Block.Instance? {
        return instances[position]
    }

    fun setBlock(position: Vector3i, id: String?) {
        if (instances.containsKey(position))
            instances.remove(position)

        if (id != null)
            instances[position] = Resources.BLOCK.get(id).create(world, position)

        val chunk = world.chunkManager.getByBlockPosition(position)
        for (z in -1..1)
            for (y in -1..1)
                for (x in -1..1) {
                    updateFaces(Vector3i(position).add(x, y, z))

                    if (chunk != null)
                        world.chunkManager.meshGenerator.addToQueue(Vector3i(chunk.position).add(x, y, z))
                }
    }


    fun update(time: SceneTime) {

    }

}