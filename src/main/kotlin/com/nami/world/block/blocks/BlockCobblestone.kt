package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemCobblestone

object BlockCobblestone : Block(id = "cobblestone") {

    override val textures = mapOf(
        Face.TOP to "block.cobblestone",
        Face.BOTTOM to "block.cobblestone",
        Face.NORTH to "block.cobblestone",
        Face.EAST to "block.cobblestone",
        Face.WEST to "block.cobblestone",
        Face.SOUTH to "block.cobblestone"
    )

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemCobblestone)
    )

}