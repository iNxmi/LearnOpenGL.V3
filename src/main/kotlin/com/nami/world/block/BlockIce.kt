package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemIce

object BlockIce : Block(id = "ice") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.ice"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.ice"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.ice"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.ice"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.ice"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.ice")
    )

    override val layer = Layer.TRANSPARENT

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemIce)
    )

}