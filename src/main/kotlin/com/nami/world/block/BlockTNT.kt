package com.nami.world.block

import com.nami.resources.Resources

object BlockTNT : Block(id = "tnt") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.tnt_top"),
        Face.BOTTOM to Resources.TEXTURE.get("block.tnt_bottom"),
        Face.NORTH to Resources.TEXTURE.get("block.tnt_side"),
        Face.EAST to Resources.TEXTURE.get("block.tnt_side"),
        Face.WEST to Resources.TEXTURE.get("block.tnt_side"),
        Face.SOUTH to Resources.TEXTURE.get("block.tnt_side")
    )

    override val resistance = mapOf(
        "tag.hand" to 0.1f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.tnt"))
    )

}