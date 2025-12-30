package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemOakPlanks

object BlockOakPlanks : Block(id = "oak_planks") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.oak_planks",
        ChunkMesh.Face.BOTTOM to "block.oak_planks",
        ChunkMesh.Face.NORTH to "block.oak_planks",
        ChunkMesh.Face.EAST to "block.oak_planks",
        ChunkMesh.Face.WEST to "block.oak_planks",
        ChunkMesh.Face.SOUTH to "block.oak_planks"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemOakPlanks)
    )

}