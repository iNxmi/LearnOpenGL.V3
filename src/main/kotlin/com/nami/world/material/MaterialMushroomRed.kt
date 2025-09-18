package com.nami.world.material

import com.nami.resources.Resources

object MaterialMushroomRed : Material(id = "mushroom_red") {

    override val textures = mapOf(
        Face.TOP to Resources.TEXTURE.get("block.mushroom_red"),
        Face.BOTTOM to Resources.TEXTURE.get("block.mushroom_red"),
        Face.NORTH to Resources.TEXTURE.get("block.mushroom_red"),
        Face.EAST to Resources.TEXTURE.get("block.mushroom_red"),
        Face.WEST to Resources.TEXTURE.get("block.mushroom_red"),
        Face.SOUTH to Resources.TEXTURE.get("block.mushroom_red")
    )

    override val resistance = mapOf(
        "item.tool.hand" to 0.0f
    )

    override val drops = setOf(
        Drop(Resources.ITEM.get("mushroom"), probability = 0.25f)
    )

}
