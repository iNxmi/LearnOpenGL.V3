package com.nami.world.material

import com.nami.resources.Resources

object MaterialGrass : Material(id = "grass") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.grass_top"),
        Face.BOTTOM to Resources.TEXTURE.get("block.dirt"),
        Face.NORTH to Resources.TEXTURE.get("block.grass_side"),
        Face.EAST to Resources.TEXTURE.get("block.grass_side"),
        Face.WEST to Resources.TEXTURE.get("block.grass_side"),
        Face.SOUTH to Resources.TEXTURE.get("block.grass_side")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val tags = setOf(
        "soil"
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.dirt"))
    )

}