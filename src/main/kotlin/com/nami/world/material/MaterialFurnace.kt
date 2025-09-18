package com.nami.world.material

import com.nami.resources.Resources

object MaterialFurnace : Material(id = "furnace") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.furnace_top"),
        Face.BOTTOM to Resources.TEXTURE.get("block.cobblestone"),
        Face.NORTH to Resources.TEXTURE.get("block.furnace_front"),
        Face.EAST to Resources.TEXTURE.get("block.furnace_side"),
        Face.WEST to Resources.TEXTURE.get("block.furnace_side"),
        Face.SOUTH to Resources.TEXTURE.get("block.furnace_side")
    )

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val tags = setOf(
        "workstation",
        "furnace"
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.furnace"))
    )

}