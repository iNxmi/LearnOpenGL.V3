package com.nami.world.block.blocks

import com.nami.world.Drop
import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.block.Layer
import com.nami.world.item.items.ItemIce

object BlockIce : Block(id = "ice") {

    override val textures = mapOf(
        Face.TOP to "block.ice",
        Face.BOTTOM to "block.ice",
        Face.NORTH to "block.ice",
        Face.EAST to "block.ice",
        Face.WEST to "block.ice",
        Face.SOUTH to "block.ice"
    )

    override val layer = Layer.TRANSPARENT

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(ItemIce)
    )

}