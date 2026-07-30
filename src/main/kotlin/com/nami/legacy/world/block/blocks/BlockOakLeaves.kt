package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.block.Layer
import com.nami.world.item.items.ItemAcorn
import com.nami.world.item.items.ItemOakLeaves
import com.nami.world.item.items.ItemStick

object BlockOakLeaves : Block(id = "oak_leaves") {

    override val textures = mapOf(
        Face.TOP to "block.oak_leaves",
        Face.BOTTOM to "block.oak_leaves",
        Face.NORTH to "block.oak_leaves",
        Face.EAST to "block.oak_leaves",
        Face.WEST to "block.oak_leaves",
        Face.SOUTH to "block.oak_leaves"
    )

    override val layer = Layer.TRANSPARENT

    override val resistance = mapOf(
        "tool.hand" to 0.25f,
        "tag.shears" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemOakLeaves),
        Drop(ItemAcorn, probability = 0.25f),
        Drop(ItemStick, probability = 0.35f)
    )

}