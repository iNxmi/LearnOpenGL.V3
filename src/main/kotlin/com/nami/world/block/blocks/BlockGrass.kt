package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemDirt

object BlockGrass : Block(id = "grass") {

    override val textures = mapOf(
        Face.TOP to "block.grass_top",
        Face.BOTTOM to "block.dirt",
        Face.NORTH to "block.grass_side",
        Face.EAST to "block.grass_side",
        Face.WEST to "block.grass_side",
        Face.SOUTH to "block.grass_side"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val tags = setOf(
        "soil"
    )

    override val drops = setOf(
        Drop(ItemDirt)
    )

}