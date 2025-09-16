package com.nami.world.block

import com.nami.resources.Resources

object BlockBirchLog : Block(id = "birch_log") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.birch_log_top"),
        Face.BOTTOM to Resources.TEXTURE.get("block.birch_log_top"),
        Face.NORTH to Resources.TEXTURE.get("block.birch_log_side"),
        Face.EAST to Resources.TEXTURE.get("block.birch_log_side"),
        Face.WEST to Resources.TEXTURE.get("block.birch_log_side"),
        Face.SOUTH to Resources.TEXTURE.get("block.birch_log_side")
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("leaf"), 1..3, 1f),
        Drop(Resources.ITEM.get("acorn"), 1..1, 0.25f),
        Drop(Resources.ITEM.get("stick"), 1..2, 0.50f)
    )

}