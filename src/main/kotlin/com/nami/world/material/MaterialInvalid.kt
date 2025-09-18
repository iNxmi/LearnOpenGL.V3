package com.nami.world.material

import com.nami.resources.Resources

object MaterialInvalid : Material(id = "invalid") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.invalid"),
        Face.BOTTOM to Resources.TEXTURE.get("block.invalid"),
        Face.NORTH to Resources.TEXTURE.get("block.invalid"),
        Face.EAST to Resources.TEXTURE.get("block.invalid"),
        Face.WEST to Resources.TEXTURE.get("block.invalid"),
        Face.SOUTH to Resources.TEXTURE.get("block.invalid")
    )

}