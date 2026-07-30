package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemBirchLog

object BlockBirchLog : Block(id = "birch_log") {

    override val textures = mapOf(
        Face.TOP to "block.birch_log_top",
        Face.BOTTOM to "block.birch_log_top",
        Face.NORTH to "block.birch_log_side",
        Face.EAST to "block.birch_log_side",
        Face.WEST to "block.birch_log_side",
        Face.SOUTH to "block.birch_log_side"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemBirchLog)
    )

}