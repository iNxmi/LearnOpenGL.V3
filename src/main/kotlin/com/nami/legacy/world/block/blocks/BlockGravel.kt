package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemFlint
import com.nami.world.item.items.ItemGravel

object BlockGravel : Block(id = "gravel") {

    override val textures = mapOf(
        Face.TOP to "block.gravel",
        Face.BOTTOM to "block.gravel",
        Face.NORTH to "block.gravel",
        Face.EAST to "block.gravel",
        Face.WEST to "block.gravel",
        Face.SOUTH to "block.gravel"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.5f,
        "tag.shovel" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemGravel),
        Drop(ItemFlint, probability = 0.35f)
    )

}