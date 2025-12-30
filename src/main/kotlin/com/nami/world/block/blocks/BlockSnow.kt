package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemSnow

object BlockSnow : Block(id = "snow" ){

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.snow",
        ChunkMesh.Face.BOTTOM to "block.snow",
        ChunkMesh.Face.NORTH to "block.snow",
        ChunkMesh.Face.EAST to "block.snow",
        ChunkMesh.Face.WEST to "block.snow",
        ChunkMesh.Face.SOUTH to "block.snow"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.25f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemSnow)
    )

}