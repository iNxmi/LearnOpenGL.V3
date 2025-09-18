package com.nami.world.material

import com.nami.resources.Resources

object MaterialCobblestone : Material(id = "cobblestone") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.cobblestone"),
        Face.BOTTOM to Resources.TEXTURE.get("block.cobblestone"),
        Face.NORTH to Resources.TEXTURE.get("block.cobblestone"),
        Face.EAST to Resources.TEXTURE.get("block.cobblestone"),
        Face.WEST to Resources.TEXTURE.get("block.cobblestone"),
        Face.SOUTH to Resources.TEXTURE.get("block.cobblestone")
    )

    override val resistance = mapOf(
        "tag.pickaxe" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("block.cobblestone"))
    )

}