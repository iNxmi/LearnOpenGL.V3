package com.nami.world.block.blocks

import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh

object BlockInvalid : Block(id = "invalid" ){

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.invalid",
        ChunkMesh.Face.BOTTOM to "block.invalid",
        ChunkMesh.Face.NORTH to "block.invalid",
        ChunkMesh.Face.EAST to "block.invalid",
        ChunkMesh.Face.WEST to "block.invalid",
        ChunkMesh.Face.SOUTH to "block.invalid"
    )

}