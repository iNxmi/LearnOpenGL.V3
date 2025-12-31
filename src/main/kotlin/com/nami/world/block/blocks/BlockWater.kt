package com.nami.world.block.blocks

import com.nami.world.block.Block
import com.nami.world.block.Face
import com.nami.world.block.Layer

object BlockWater : Block(id = "water") {

    override val textures = mapOf(
        Face.TOP to "block.water",
        Face.BOTTOM to "block.water",
        Face.NORTH to "block.water",
        Face.EAST to "block.water",
        Face.WEST to "block.water",
        Face.SOUTH to "block.water"
    )

    override val layer = Layer.FLUID

}