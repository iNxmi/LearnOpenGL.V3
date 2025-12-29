package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemSnow

object BlockSnow : Block(id = "snow") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.snow"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.snow"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.snow"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.snow"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.snow"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.snow")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.25f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemSnow)
    )

}