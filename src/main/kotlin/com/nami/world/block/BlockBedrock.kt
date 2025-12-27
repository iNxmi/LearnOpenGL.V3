package com.nami.world.block

import com.nami.resources.Resources

object BlockBedrock : Block(id = "bedrock") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.bedrock"),
        Face.BOTTOM to Resources.TEXTURE.get("block.bedrock"),
        Face.NORTH to Resources.TEXTURE.get("block.bedrock"),
        Face.EAST to Resources.TEXTURE.get("block.bedrock"),
        Face.WEST to Resources.TEXTURE.get("block.bedrock"),
        Face.SOUTH to Resources.TEXTURE.get("block.bedrock")
    )

}