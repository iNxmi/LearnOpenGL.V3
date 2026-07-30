package com.nami.world.block.blocks

import com.nami.world.block.Block
import com.nami.world.block.Face

object BlockBedrock : Block(id = "bedrock") {

    override val textures = mapOf(
        Face.TOP to "block.bedrock",
        Face.BOTTOM to "block.bedrock",
        Face.NORTH to "block.bedrock",
        Face.EAST to "block.bedrock",
        Face.WEST to "block.bedrock",
        Face.SOUTH to "block.bedrock"
    )

}