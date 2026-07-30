package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemBirchPlanks

object BlockBirchPlanks : Block(id = "birch_planks") {

    override val textures = mapOf(
        Face.TOP to "block.birch_planks",
        Face.BOTTOM to "block.birch_planks",
        Face.NORTH to "block.birch_planks",
        Face.EAST to "block.birch_planks",
        Face.WEST to "block.birch_planks",
        Face.SOUTH to "block.birch_planks"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemBirchPlanks)
    )

}