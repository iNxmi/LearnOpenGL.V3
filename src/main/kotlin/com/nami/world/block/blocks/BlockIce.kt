package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Layer
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemIce

object BlockIce : Block(id = "ice") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.ice",
        ChunkMesh.Face.BOTTOM to "block.ice",
        ChunkMesh.Face.NORTH to "block.ice",
        ChunkMesh.Face.EAST to "block.ice",
        ChunkMesh.Face.WEST to "block.ice",
        ChunkMesh.Face.SOUTH to "block.ice"
    )

    override val layer = Layer.TRANSPARENT

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemIce)
    )

}