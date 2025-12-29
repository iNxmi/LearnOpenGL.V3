package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemMushroom

object BlockMushroomStem : Block(id = "mushroom_stem") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.mushroom_stem"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.mushroom_stem"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.mushroom_stem"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.mushroom_stem"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.mushroom_stem"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.mushroom_stem")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemMushroom, probability = 0.25f)
    )

}
