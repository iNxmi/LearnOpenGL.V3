package com.nami.world.block

import org.joml.Vector2i
import org.joml.Vector3i

interface BlockManager {

    fun getHeight(position: Vector2i, start: Int, types: Set<Layer>): Int

    fun updateFaces(position: Vector3i)

    fun getBlock(position: Vector3i): Block?
    fun setBlock(position: Vector3i, block: Block)
    fun setBlocks(blocks: Map<Vector3i, Block>)

}