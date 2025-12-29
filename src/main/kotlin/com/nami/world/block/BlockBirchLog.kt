package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemBirchLog

object BlockBirchLog : Block(id = "birch_log") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.birch_log_top"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.birch_log_top"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.birch_log_side"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.birch_log_side"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.birch_log_side"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.birch_log_side")
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemBirchLog)
    )

}