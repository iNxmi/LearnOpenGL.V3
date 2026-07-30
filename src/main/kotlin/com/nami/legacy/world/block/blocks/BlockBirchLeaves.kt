package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.block.Layer
import com.nami.world.item.items.ItemAcorn
import com.nami.world.item.items.ItemBirchLeaves
import com.nami.world.item.items.ItemStick

object BlockBirchLeaves : Block(id = "birch_leaves" ){

    override val textures = mapOf(
        Face.TOP to "block.birch_leaves",
        Face.BOTTOM to "block.birch_leaves",
        Face.NORTH to "block.birch_leaves",
        Face.EAST to "block.birch_leaves",
        Face.WEST to "block.birch_leaves",
        Face.SOUTH to "block.birch_leaves"
    )

    override val layer = Layer.FOLIAGE

    override val resistance = mapOf(
        "tool.hand" to 0.25f,
        "tag.shears" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemBirchLeaves),
        Drop(ItemAcorn, probability = 0.25f),
        Drop(ItemStick, probability = 0.35f)
    )

}