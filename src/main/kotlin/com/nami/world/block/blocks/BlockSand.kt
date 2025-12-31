package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemSand

object BlockSand : Block(id = "sand") {

    override val textures = mapOf(
        Face.TOP to "block.sand",
        Face.BOTTOM to "block.sand",
        Face.NORTH to "block.sand",
        Face.EAST to "block.sand",
        Face.WEST to "block.sand",
        Face.SOUTH to "block.sand"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemSand)
    )

}