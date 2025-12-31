package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemOakPlanks

object BlockOakPlanks : Block(id = "oak_planks") {

    override val textures = mapOf(
        Face.TOP to "block.oak_planks",
        Face.BOTTOM to "block.oak_planks",
        Face.NORTH to "block.oak_planks",
        Face.EAST to "block.oak_planks",
        Face.WEST to "block.oak_planks",
        Face.SOUTH to "block.oak_planks"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemOakPlanks)
    )

}