package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemSand

object BlockSand : Block(id = "sand") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.sand",
        ChunkMesh.Face.BOTTOM to "block.sand",
        ChunkMesh.Face.NORTH to "block.sand",
        ChunkMesh.Face.EAST to "block.sand",
        ChunkMesh.Face.WEST to "block.sand",
        ChunkMesh.Face.SOUTH to "block.sand"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemSand)
    )

}