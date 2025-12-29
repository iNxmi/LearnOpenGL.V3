package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemSand

object BlockSand : Block(id = "sand") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.sand"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.sand"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.sand"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.sand"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.sand"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.sand")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemSand)
    )

}