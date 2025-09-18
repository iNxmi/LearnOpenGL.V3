package com.nami.world.material

import com.nami.resources.Resources

object MaterialIce : Material(id = "ice") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.ice"),
        Face.BOTTOM to Resources.TEXTURE.get("block.ice"),
        Face.NORTH to Resources.TEXTURE.get("block.ice"),
        Face.EAST to Resources.TEXTURE.get("block.ice"),
        Face.WEST to Resources.TEXTURE.get("block.ice"),
        Face.SOUTH to Resources.TEXTURE.get("block.ice")
    )

    override val layer = Layer.TRANSPARENT

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.ice"))
    )

}