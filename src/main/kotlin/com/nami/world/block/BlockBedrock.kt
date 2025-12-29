package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.chunk.ChunkMesh

object BlockBedrock : Block(id = "bedrock") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.bedrock"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.bedrock"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.bedrock"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.bedrock"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.bedrock"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.bedrock")
    )

}