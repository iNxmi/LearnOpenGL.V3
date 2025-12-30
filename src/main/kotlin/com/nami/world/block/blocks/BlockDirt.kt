package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemDirt

object BlockDirt : Block(id = "dirt" ){

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.dirt",
        ChunkMesh.Face.BOTTOM to "block.dirt",
        ChunkMesh.Face.NORTH to "block.dirt",
        ChunkMesh.Face.EAST to "block.dirt",
        ChunkMesh.Face.WEST to "block.dirt",
        ChunkMesh.Face.SOUTH to "block.dirt"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemDirt)
    )

}