package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemFurnace

object BlockFurnace : Block(id = "furnace" ){

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.furnace_top",
        ChunkMesh.Face.BOTTOM to "block.cobblestone",
        ChunkMesh.Face.NORTH to "block.furnace_front",
        ChunkMesh.Face.EAST to "block.furnace_side",
        ChunkMesh.Face.WEST to "block.furnace_side",
        ChunkMesh.Face.SOUTH to "block.furnace_side"
    )

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val tags = setOf(
        "workstation",
        "furnace"
    )

    override val drops = setOf(
        Drop(ItemFurnace)
    )

}