package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.chunk.ChunkMesh

object BlockInvalid : Block(id = "invalid") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.invalid"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.invalid"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.invalid"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.invalid"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.invalid"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.invalid")
    )

}