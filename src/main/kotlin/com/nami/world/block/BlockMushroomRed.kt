package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemMushroom

object BlockMushroomRed : Block(id = "mushroom_red") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.mushroom_red"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.mushroom_red"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.mushroom_red"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.mushroom_red"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.mushroom_red"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.mushroom_red")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemMushroom, probability = 0.25f)
    )

}
