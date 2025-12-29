package com.nami.world.block

import com.nami.resources.Resources
import com.nami.world.Drop
import com.nami.world.chunk.ChunkMesh
import com.nami.world.item.ItemAcorn
import com.nami.world.item.ItemBirchLeaves
import com.nami.world.item.ItemStick

object BlockBirchLeaves : Block(id = "birch_leaves") {

    override val textures = mapOf(
        ChunkMesh.Face.TOP to Resources.TEXTURE.get("block.birch_leaves"),
        ChunkMesh.Face.BOTTOM to Resources.TEXTURE.get("block.birch_leaves"),
        ChunkMesh.Face.NORTH to Resources.TEXTURE.get("block.birch_leaves"),
        ChunkMesh.Face.EAST to Resources.TEXTURE.get("block.birch_leaves"),
        ChunkMesh.Face.WEST to Resources.TEXTURE.get("block.birch_leaves"),
        ChunkMesh.Face.SOUTH to Resources.TEXTURE.get("block.birch_leaves")
    )

    override val layer = Layer.FOLIAGE

    override val resistance = mapOf(
        "tool.hand" to 0.25f,
        "tag.shears" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemBirchLeaves),
        Drop(ItemAcorn, probability = 0.25f),
        Drop(ItemStick, probability = 0.35f)
    )

}