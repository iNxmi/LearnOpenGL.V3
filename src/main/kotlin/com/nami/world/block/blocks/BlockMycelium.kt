package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemDirt

object BlockMycelium : Block(id = "mycelium" ){

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.mycelium_top",
        ChunkMesh.Face.BOTTOM to "block.dirt",
        ChunkMesh.Face.NORTH to "block.mycelium_side",
        ChunkMesh.Face.EAST to "block.mycelium_side",
        ChunkMesh.Face.WEST to "block.mycelium_side",
        ChunkMesh.Face.SOUTH to "block.mycelium_side"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemDirt)
    )

}