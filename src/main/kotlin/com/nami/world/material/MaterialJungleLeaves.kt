package com.nami.world.material

import com.nami.resources.Resources

object MaterialJungleLeaves : Material(id = "jungle_leaves") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.jungle_leaves"),
        Face.BOTTOM to Resources.TEXTURE.get("block.jungle_leaves"),
        Face.NORTH to Resources.TEXTURE.get("block.jungle_leaves"),
        Face.EAST to Resources.TEXTURE.get("block.jungle_leaves"),
        Face.WEST to Resources.TEXTURE.get("block.jungle_leaves"),
        Face.SOUTH to Resources.TEXTURE.get("block.jungle_leaves")
    )

    override val layer = Layer.FOLIAGE

    override val resistance = mapOf(
        "tool.hand" to 0.25f,
        "tag.shears" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("leaf"), amount = 1..3),
        Drop(Resources.ITEM.get("acorn"), probability = 0.25f),
        Drop(Resources.ITEM.get("stick"), probability = 0.35f)
    )

}