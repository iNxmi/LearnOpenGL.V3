package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.chunk.ChunkMesh

object BlockWater : Block(id = "water") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.water"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.water"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.water"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.water"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.water"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.water")
    )

    override val layer = Layer.FLUID

}