package com.nami.world.block

import com.nami.resources.Resources

object BlockCactus : Block(id = "cactus") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.cactus_top"),
        Face.BOTTOM to Resources.TEXTURE.get("block.cactus_bottom"),
        Face.NORTH to Resources.TEXTURE.get("block.cactus_side"),
        Face.EAST to Resources.TEXTURE.get("block.cactus_side"),
        Face.WEST to Resources.TEXTURE.get("block.cactus_side"),
        Face.SOUTH to Resources.TEXTURE.get("block.cactus_side")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.8f,
        "tag.hoe" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.cactus"))
    )

}