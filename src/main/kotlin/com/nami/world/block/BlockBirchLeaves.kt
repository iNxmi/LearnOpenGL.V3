package com.nami.world.block

import com.nami.resources.Resources

object BlockBirchLeaves : Block(id = "birch_leaves") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.birch_leaves"),
        Face.BOTTOM to Resources.TEXTURE.get("block.birch_leaves"),
        Face.NORTH to Resources.TEXTURE.get("block.birch_leaves"),
        Face.EAST to Resources.TEXTURE.get("block.birch_leaves"),
        Face.WEST to Resources.TEXTURE.get("block.birch_leaves"),
        Face.SOUTH to Resources.TEXTURE.get("block.birch_leaves")
    )

    override val layer = Layer.FOLIAGE

    override val resistance = mapOf(
        "tool.hand" to 0.25f,
        "tag.shears" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.birch_log"))
    )

}