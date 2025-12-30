package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemMushroom

object BlockMushroomYellow : Block(id = "mushroom_yellow") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.mushroom_yellow",
        ChunkMesh.Face.BOTTOM to "block.mushroom_yellow",
        ChunkMesh.Face.NORTH to "block.mushroom_yellow",
        ChunkMesh.Face.EAST to "block.mushroom_yellow",
        ChunkMesh.Face.WEST to "block.mushroom_yellow",
        ChunkMesh.Face.SOUTH to "block.mushroom_yellow"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemMushroom, probability = 0.25f)
    )

}
