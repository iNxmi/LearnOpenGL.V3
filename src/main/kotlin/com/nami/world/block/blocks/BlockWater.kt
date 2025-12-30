package com.nami.world.block.blocks

import com.nami.world.block.Block
import com.nami.world.block.Layer
import com.nami.world.chunk.ChunkMesh

object BlockWater : Block(id = "water") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.water",
        ChunkMesh.Face.BOTTOM to "block.water",
        ChunkMesh.Face.NORTH to "block.water",
        ChunkMesh.Face.EAST to "block.water",
        ChunkMesh.Face.WEST to "block.water",
        ChunkMesh.Face.SOUTH to "block.water"
    )

    override val layer = Layer.FLUID

}