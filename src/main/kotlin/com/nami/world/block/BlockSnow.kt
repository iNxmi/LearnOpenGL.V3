package com.nami.world.block

import com.nami.resources.Resources

object BlockSnow : Block(id = "snow") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.snow"),
        Face.BOTTOM to Resources.TEXTURE.get("block.snow"),
        Face.NORTH to Resources.TEXTURE.get("block.snow"),
        Face.EAST to Resources.TEXTURE.get("block.snow"),
        Face.WEST to Resources.TEXTURE.get("block.snow"),
        Face.SOUTH to Resources.TEXTURE.get("block.snow")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.25f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.snow"))
    )

}