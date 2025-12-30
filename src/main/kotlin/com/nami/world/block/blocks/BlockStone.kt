package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemCobblestone

object BlockStone : Block(id = "stone" ){

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.stone",
        ChunkMesh.Face.BOTTOM to "block.stone",
        ChunkMesh.Face.NORTH to "block.stone",
        ChunkMesh.Face.EAST to "block.stone",
        ChunkMesh.Face.WEST to "block.stone",
        ChunkMesh.Face.SOUTH to "block.stone"
    )

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemCobblestone)
    )

}