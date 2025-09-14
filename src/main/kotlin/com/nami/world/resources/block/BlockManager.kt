package com.nami.world.resources.block

import org.joml.Vector2i
import org.joml.Vector3i

interface BlockManager {

    fun getHeight(position: Vector2i, start: Int, types: Set<Block.Layer>): Int

    fun updateFaces(position: Vector3i)

    fun getBlock(position: Vector3i): Block.Instance?
    fun setBlock(position: Vector3i, block: Block.Instance?)
    fun setBlocks(blocks: Map<Vector3i, Block.Instance?>)

}