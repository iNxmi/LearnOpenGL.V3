package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemTnt

object BlockTNT : Block(id = "tnt") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.tnt_top",
        ChunkMesh.Face.BOTTOM to "block.tnt_bottom",
        ChunkMesh.Face.NORTH to "block.tnt_side",
        ChunkMesh.Face.EAST to "block.tnt_side",
        ChunkMesh.Face.WEST to "block.tnt_side",
        ChunkMesh.Face.SOUTH to "block.tnt_side"
    )

    override val resistance = mapOf(
        "tag.hand" to 0.1f
    )

    override val drops = setOf(
        Drop(ItemTnt)
    )

}