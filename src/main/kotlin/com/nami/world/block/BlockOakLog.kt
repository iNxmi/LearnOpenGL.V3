package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemOakLog

object BlockOakLog : Block(id = "oak_log") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.oak_log_top"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.oak_log_top"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.oak_log_side"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.oak_log_side"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.oak_log_side"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.oak_log_side")
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemOakLog)
    )

}