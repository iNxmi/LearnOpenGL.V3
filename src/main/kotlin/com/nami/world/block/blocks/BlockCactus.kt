package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.item.items.ItemCactus

object BlockCactus : Block(id = "cactus" ){

    override val textures = mapOf(
        Face.TOP to "block.cactus_top",
        Face.BOTTOM to "block.cactus_bottom",
        Face.NORTH to "block.cactus_side",
        Face.EAST to "block.cactus_side",
        Face.WEST to "block.cactus_side",
        Face.SOUTH to "block.cactus_side"
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.8f,
        "tag.hoe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemCactus)
    )

}