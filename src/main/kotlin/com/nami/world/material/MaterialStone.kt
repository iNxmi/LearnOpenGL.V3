package com.nami.world.material

import com.nami.resources.Resources

object MaterialStone : Material(id = "stone") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.stone"),
        Face.BOTTOM to Resources.TEXTURE.get("block.stone"),
        Face.NORTH to Resources.TEXTURE.get("block.stone"),
        Face.EAST to Resources.TEXTURE.get("block.stone"),
        Face.WEST to Resources.TEXTURE.get("block.stone"),
        Face.SOUTH to Resources.TEXTURE.get("block.stone")
    )

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.cobblestone"))
    )

}