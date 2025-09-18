package com.nami.world.material

import com.nami.resources.Resources

object MaterialGravel : Material(id = "gravel") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.gravel"),
        Face.BOTTOM to Resources.TEXTURE.get("block.gravel"),
        Face.NORTH to Resources.TEXTURE.get("block.gravel"),
        Face.EAST to Resources.TEXTURE.get("block.gravel"),
        Face.WEST to Resources.TEXTURE.get("block.gravel"),
        Face.SOUTH to Resources.TEXTURE.get("block.gravel")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.gravel")),
        Drop(Resources.ITEM.get("flint"), probability = 0.35f)
    )

}