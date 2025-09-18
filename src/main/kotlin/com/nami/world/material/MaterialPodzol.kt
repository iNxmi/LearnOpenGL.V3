package com.nami.world.material

import com.nami.resources.Resources

object MaterialPodzol : Material(id = "podzol") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.podzol_top"),
        Face.BOTTOM to Resources.TEXTURE.get("block.dirt"),
        Face.NORTH to Resources.TEXTURE.get("block.podzol_side"),
        Face.EAST to Resources.TEXTURE.get("block.podzol_side"),
        Face.WEST to Resources.TEXTURE.get("block.podzol_side"),
        Face.SOUTH to Resources.TEXTURE.get("block.podzol_side")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.dirt"))
    )

}