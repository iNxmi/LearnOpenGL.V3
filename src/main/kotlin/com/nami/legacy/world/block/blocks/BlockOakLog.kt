package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemOakLog

object BlockOakLog : Block(id = "oak_log" ){

    override val textures = mapOf(
        Face.TOP to "block.oak_log_top",
        Face.BOTTOM to "block.oak_log_top",
        Face.NORTH to "block.oak_log_side",
        Face.EAST to "block.oak_log_side",
        Face.WEST to "block.oak_log_side",
        Face.SOUTH to "block.oak_log_side"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemOakLog)
    )

}