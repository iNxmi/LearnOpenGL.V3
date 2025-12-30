package com.nami.world.block.blocks

import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh

object BlockBedrock : Block(id = "bedrock") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.bedrock",
        ChunkMesh.Face.BOTTOM to "block.bedrock",
        ChunkMesh.Face.NORTH to "block.bedrock",
        ChunkMesh.Face.EAST to "block.bedrock",
        ChunkMesh.Face.WEST to "block.bedrock",
        ChunkMesh.Face.SOUTH to "block.bedrock"
    )

}