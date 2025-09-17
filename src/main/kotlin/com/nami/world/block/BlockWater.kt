package com.nami.world.block

import com.nami.resources.Resources

object BlockWater : Block(id = "water") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.water"),
        Face.BOTTOM to Resources.TEXTURE.get("block.water"),
        Face.NORTH to Resources.TEXTURE.get("block.water"),
        Face.EAST to Resources.TEXTURE.get("block.water"),
        Face.WEST to Resources.TEXTURE.get("block.water"),
        Face.SOUTH to Resources.TEXTURE.get("block.water")
    )

    override val layer = Layer.FLUID

}