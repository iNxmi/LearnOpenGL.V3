package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemOakLog

object BlockOakLog : Block(id = "oak_log" ){

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.oak_log_top",
        ChunkMesh.Face.BOTTOM to "block.oak_log_top",
        ChunkMesh.Face.NORTH to "block.oak_log_side",
        ChunkMesh.Face.EAST to "block.oak_log_side",
        ChunkMesh.Face.WEST to "block.oak_log_side",
        ChunkMesh.Face.SOUTH to "block.oak_log_side"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemOakLog)
    )

}