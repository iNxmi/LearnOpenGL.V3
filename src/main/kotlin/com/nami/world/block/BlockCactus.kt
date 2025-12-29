package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemCactus

object BlockCactus : Block(id = "cactus") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.cactus_top"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.cactus_bottom"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.cactus_side"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.cactus_side"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.cactus_side"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.cactus_side")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.8f,
        "tag.hoe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemCactus)
    )

}