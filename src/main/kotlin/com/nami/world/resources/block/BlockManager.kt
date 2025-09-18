package com.nami.world.resources.block

import com.nami.world.material.Material
import com.nami.world.material.Layer
import org.joml.Vector2i
import org.joml.Vector3i

interface BlockManager {

    fun getHeight(position: Vector2i, start: Int, types: Set<Layer>): Int

    fun updateFaces(position: Vector3i)

    fun getBlock(position: Vector3i): Material?
    fun setBlock(position: Vector3i, material: Material)
    fun setBlocks(blocks: Map<Vector3i, Material>)

}