package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemTnt

object BlockTNT : Block(id = "tnt") {

    override val textures = mapOf(
        Face.TOP to "block.tnt_top",
        Face.BOTTOM to "block.tnt_bottom",
        Face.NORTH to "block.tnt_side",
        Face.EAST to "block.tnt_side",
        Face.WEST to "block.tnt_side",
        Face.SOUTH to "block.tnt_side"
    )

    override val resistance = mapOf(
        "tag.hand" to 0.1f
    )

    override val drops = setOf(
        Drop(ItemTnt)
    )

}