package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemMushroom

object BlockMushroomStem : Block(id = "mushroom_stem") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.mushroom_stem",
        ChunkMesh.Face.BOTTOM to "block.mushroom_stem",
        ChunkMesh.Face.NORTH to "block.mushroom_stem",
        ChunkMesh.Face.EAST to "block.mushroom_stem",
        ChunkMesh.Face.WEST to "block.mushroom_stem",
        ChunkMesh.Face.SOUTH to "block.mushroom_stem"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemMushroom, probability = 0.25f)
    )

}
