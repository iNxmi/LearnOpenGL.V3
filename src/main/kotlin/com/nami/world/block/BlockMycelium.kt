package com.nami.world.block

import com.nami.resources.Resources

object BlockMycelium : Block(id = "mycelium") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.mycelium_top"),
        Face.BOTTOM to Resources.TEXTURE.get("block.dirt"),
        Face.NORTH to Resources.TEXTURE.get("block.mycelium_side"),
        Face.EAST to Resources.TEXTURE.get("block.mycelium_side"),
        Face.WEST to Resources.TEXTURE.get("block.mycelium_side"),
        Face.SOUTH to Resources.TEXTURE.get("block.mycelium_side")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.dirt"))
    )

}