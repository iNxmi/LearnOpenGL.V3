package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemCactus

object BlockCactus : Block(id = "cactus" ){

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.cactus_top",
        ChunkMesh.Face.BOTTOM to "block.cactus_bottom",
        ChunkMesh.Face.NORTH to "block.cactus_side",
        ChunkMesh.Face.EAST to "block.cactus_side",
        ChunkMesh.Face.WEST to "block.cactus_side",
        ChunkMesh.Face.SOUTH to "block.cactus_side"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.8f,
        "tag.hoe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemCactus)
    )

}