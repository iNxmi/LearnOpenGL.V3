package com.nami.world.block

import com.nami.resources.Resources

object BlockSand : Block(id = "sand") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.sand"),
        Face.BOTTOM to Resources.TEXTURE.get("block.sand"),
        Face.NORTH to Resources.TEXTURE.get("block.sand"),
        Face.EAST to Resources.TEXTURE.get("block.sand"),
        Face.WEST to Resources.TEXTURE.get("block.sand"),
        Face.SOUTH to Resources.TEXTURE.get("block.sand")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.sand"))
    )

}