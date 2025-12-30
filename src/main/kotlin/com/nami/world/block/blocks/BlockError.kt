package com.nami.world.block.blocks

import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh

object BlockError : Block(id = "error") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "error",
        ChunkMesh.Face.BOTTOM to "error",
        ChunkMesh.Face.NORTH to "error",
        ChunkMesh.Face.EAST to "error",
        ChunkMesh.Face.WEST to "error",
        ChunkMesh.Face.SOUTH to "error"
    )

}