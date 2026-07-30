package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemJunglePlanks

object BlockJunglePlanks : Block(id = "jungle_planks") {

    override val textures = mapOf(
        Face.TOP to "block.jungle_planks",
        Face.BOTTOM to "block.jungle_planks",
        Face.NORTH to "block.jungle_planks",
        Face.EAST to "block.jungle_planks",
        Face.WEST to "block.jungle_planks",
        Face.SOUTH to "block.jungle_planks"
    )

    override val resistance = mapOf(
        "tag.axe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemJunglePlanks)
    )

}