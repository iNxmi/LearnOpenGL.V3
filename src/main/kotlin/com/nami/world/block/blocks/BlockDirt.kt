package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemDirt

object BlockDirt : Block(id = "dirt" ){

    override val textures = mapOf(
        Face.TOP to "block.dirt",
        Face.BOTTOM to "block.dirt",
        Face.NORTH to "block.dirt",
        Face.EAST to "block.dirt",
        Face.WEST to "block.dirt",
        Face.SOUTH to "block.dirt"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemDirt)
    )

}