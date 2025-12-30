package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemJunglePlanks

object BlockJunglePlanks : Block(id = "jungle_planks") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.jungle_planks",
        ChunkMesh.Face.BOTTOM to "block.jungle_planks",
        ChunkMesh.Face.NORTH to "block.jungle_planks",
        ChunkMesh.Face.EAST to "block.jungle_planks",
        ChunkMesh.Face.WEST to "block.jungle_planks",
        ChunkMesh.Face.SOUTH to "block.jungle_planks"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemJunglePlanks)
    )

}