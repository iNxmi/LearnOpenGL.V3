package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemDirt

object BlockMycelium : Block(id = "mycelium" ){

    override val textures = mapOf(
        Face.TOP to "block.mycelium_top",
        Face.BOTTOM to "block.dirt",
        Face.NORTH to "block.mycelium_side",
        Face.EAST to "block.mycelium_side",
        Face.WEST to "block.mycelium_side",
        Face.SOUTH to "block.mycelium_side"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemDirt)
    )

}