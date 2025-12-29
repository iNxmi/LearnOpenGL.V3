package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemFurnace

object BlockFurnace : Block(id = "furnace") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.furnace_top"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.cobblestone"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.furnace_front"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.furnace_side"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.furnace_side"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.furnace_side")
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