package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemMushroom

object BlockMushroomYellow : Block(id = "mushroom_yellow") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.mushroom_yellow"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.mushroom_yellow"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.mushroom_yellow"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.mushroom_yellow"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.mushroom_yellow"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.mushroom_yellow")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemMushroom, probability = 0.25f)
    )

}
