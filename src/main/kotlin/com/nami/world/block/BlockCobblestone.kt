package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemCobblestone

object BlockCobblestone : Block(id = "cobblestone") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.cobblestone"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.cobblestone"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.cobblestone"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.cobblestone"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.cobblestone"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.cobblestone")
    )

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemCobblestone)
    )

}