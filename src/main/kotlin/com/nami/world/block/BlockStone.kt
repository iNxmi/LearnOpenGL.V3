package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemCobblestone

object BlockStone : Block(id = "stone") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.stone"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.stone"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.stone"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.stone"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.stone"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.stone")
    )

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemCobblestone)
    )

}