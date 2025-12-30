package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.items.ItemBirchLog

object BlockBirchLog : Block(id = "birch_log") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to "block.birch_log_top",
        ChunkMesh.Face.BOTTOM to "block.birch_log_top",
        ChunkMesh.Face.NORTH to "block.birch_log_side",
        ChunkMesh.Face.EAST to "block.birch_log_side",
        ChunkMesh.Face.WEST to "block.birch_log_side",
        ChunkMesh.Face.SOUTH to "block.birch_log_side"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemBirchLog)
    )

}