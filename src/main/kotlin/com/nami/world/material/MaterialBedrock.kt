package com.nami.world.material

import com.nami.resources.Resources
import com.nami.world.block.Face

object MaterialBedrock : Material(id = "bedrock") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.bedrock"),
        Face.BOTTOM to Resources.TEXTURE.get("block.bedrock"),
        Face.NORTH to Resources.TEXTURE.get("block.bedrock"),
        Face.EAST to Resources.TEXTURE.get("block.bedrock"),
        Face.WEST to Resources.TEXTURE.get("block.bedrock"),
        Face.SOUTH to Resources.TEXTURE.get("block.bedrock")
    )

}