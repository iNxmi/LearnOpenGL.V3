package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemSnow

object BlockSnow : Block(id = "snow" ){

    override val textures = mapOf(
        Face.TOP to "block.snow",
        Face.BOTTOM to "block.snow",
        Face.NORTH to "block.snow",
        Face.EAST to "block.snow",
        Face.WEST to "block.snow",
        Face.SOUTH to "block.snow"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.25f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemSnow)
    )

}