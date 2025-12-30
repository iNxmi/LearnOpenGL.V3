package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemCobblestone

object BlockCobblestone : Block(id = "cobblestone") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.cobblestone",
        ChunkMesh.Face.BOTTOM to "block.cobblestone",
        ChunkMesh.Face.NORTH to "block.cobblestone",
        ChunkMesh.Face.EAST to "block.cobblestone",
        ChunkMesh.Face.WEST to "block.cobblestone",
        ChunkMesh.Face.SOUTH to "block.cobblestone"
    )

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemCobblestone)
    )

}