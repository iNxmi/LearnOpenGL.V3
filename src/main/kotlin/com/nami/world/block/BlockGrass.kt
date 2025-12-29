package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemDirt

object BlockGrass : Block(id = "grass") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.grass_top"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.dirt"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.grass_side"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.grass_side"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.grass_side"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.grass_side")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val tags = setOf(
        "soil"
    )

    override val drops = setOf(
        Drop(ItemDirt)
    )

}