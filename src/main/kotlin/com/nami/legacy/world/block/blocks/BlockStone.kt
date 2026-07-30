package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemCobblestone

object BlockStone : Block(id = "stone" ){

    override val textures = mapOf(
        Face.TOP to "block.stone",
        Face.BOTTOM to "block.stone",
        Face.NORTH to "block.stone",
        Face.EAST to "block.stone",
        Face.WEST to "block.stone",
        Face.SOUTH to "block.stone"
    )

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemCobblestone)
    )

}