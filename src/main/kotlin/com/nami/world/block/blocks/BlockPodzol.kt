package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemDirt

object BlockPodzol : Block(id = "podzol") {

    override val textures = mapOf(
        Face.TOP to "block.podzol_top",
        Face.BOTTOM to "block.dirt",
        Face.NORTH to "block.podzol_side",
        Face.EAST to "block.podzol_side",
        Face.WEST to "block.podzol_side",
        Face.SOUTH to "block.podzol_side"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemDirt)
    )

}