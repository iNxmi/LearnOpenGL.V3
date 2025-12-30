package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Layer
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemAcorn
import com.nami.world.item.items.ItemOakLeaves
import com.nami.world.item.items.ItemStick

object BlockOakLeaves : Block(id = "oak_leaves") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.oak_leaves",
        ChunkMesh.Face.BOTTOM to "block.oak_leaves",
        ChunkMesh.Face.NORTH to "block.oak_leaves",
        ChunkMesh.Face.EAST to "block.oak_leaves",
        ChunkMesh.Face.WEST to "block.oak_leaves",
        ChunkMesh.Face.SOUTH to "block.oak_leaves"
    )

    override val layer = Layer.FOLIAGE

    override val resistance = mapOf(
        "tool.hand" to 0.25f,
        "tag.shears" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemOakLeaves),
        Drop(ItemAcorn, probability = 0.25f),
        Drop(ItemStick, probability = 0.35f)
    )

}