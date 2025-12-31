package com.nami.world.block.blocks

import com.nami.world.block.Block
import com.nami.world.block.Face

object BlockError : Block(id = "error") {

    override val textures = mapOf(
        Face.TOP to "error",
        Face.BOTTOM to "error",
        Face.NORTH to "error",
        Face.EAST to "error",
        Face.WEST to "error",
        Face.SOUTH to "error"
    )

}