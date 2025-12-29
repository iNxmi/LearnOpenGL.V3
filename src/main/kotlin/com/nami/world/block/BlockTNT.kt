package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemTnt

object BlockTNT : Block(id = "tnt") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.tnt_top"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.tnt_bottom"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.tnt_side"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.tnt_side"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.tnt_side"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.tnt_side")
    )

    override val resistance = mapOf(
        "tag.hand" to 0.1f
    )

    override val drops = setOf(
        Drop(ItemTnt)
    )

}